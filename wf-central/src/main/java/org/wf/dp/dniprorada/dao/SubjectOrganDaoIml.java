package org.wf.dp.dniprorada.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.Subject;
import org.wf.dp.dniprorada.model.SubjectOrgan;
import org.wf.dp.dniprorada.model.SubjectOrganJoin;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Repository
public class SubjectOrganDaoIml extends GenericEntityDao<SubjectOrgan> implements SubjectOrganDao {

    @Autowired
    @Qualifier("subjectOrganJoinDao")
    private GenericEntityDao<SubjectOrganJoin> subjectOrganJoinDao;

    protected SubjectOrganDaoIml() {
        super(SubjectOrgan.class);
    }

    @Override
    public SubjectOrgan getSubjectOrgan(String sOKPO) {
        return findBy("sOKPO", sOKPO).orNull();
    }

    @Override
    public SubjectOrgan setSubjectOrgan(String sOKPO) {
        SubjectOrgan oSubjectOrgan = new SubjectOrgan();
        oSubjectOrgan.setsOKPO(sOKPO);
        Subject oSubject = new Subject();
        oSubjectOrgan.setoSubject(oSubject);
        return saveOrUpdateSubjectOrgan(oSubjectOrgan);
    }

    @Override
    public SubjectOrgan saveOrUpdateSubjectOrgan(SubjectOrgan oSubjectOrgan) {
        oSubjectOrgan.getoSubject().setsID(oSubjectOrgan.getsOKPO());
        getSession().saveOrUpdate(oSubjectOrgan);
        return oSubjectOrgan;
    }

    @Override
    public SubjectOrgan getSubjectOrgan(Long nID) {
        return findById(nID).orNull();
    }

    @SuppressWarnings("unchecked" /* православно тут все... */)
    public List<SubjectOrganJoin> findSubjectOrganJoinsBy(Long organID, Long regionID, Long cityID, String uaID) {
        Criteria crt = getSession()
                .createCriteria(SubjectOrganJoin.class)
                .add(Restrictions.eq("subjectOrganId", organID));

        if (regionID != null && regionID > 0)
            crt.add(Restrictions.eq("regionId", regionID));

        if (cityID != null && cityID > 0)
            crt.add(Restrictions.eq("cityId", cityID));

        if (isNotBlank(uaID))
            crt.add(Restrictions.eq("uaId", uaID));

        return crt.list();
    }

    @Transactional // TODO Might be bottleneck, should be replaced via stored procedure.
    public void add(SubjectOrganJoin soj) {

        SubjectOrganJoin persisted = get(soj.getPublicId(), soj.getSubjectOrganId());

        if (persisted == null) {
            // Object doesn't exists, we have to create it
            soj.setId(null);
            subjectOrganJoinDao.saveOrUpdate(soj);
        } else {
            // Object available, hence, we have to update its main parameters
            persisted.setUaId(soj.getUaId());
            persisted.setNameUa(soj.getNameUa());
            persisted.setNameRu(soj.getNameRu());
            persisted.setGeoLongitude(soj.getGeoLongitude());
            persisted.setGeoLatitude(soj.getGeoLatitude());

            if (soj.getRegionId() != null)
                persisted.setRegionId(soj.getRegionId());

            if (soj.getCityId() != null)
                persisted.setCityId(soj.getCityId());

            subjectOrganJoinDao.saveOrUpdate(persisted);
        }
    }

    private SubjectOrganJoin get(String publicId, Long subjectOrganId) {
        return (SubjectOrganJoin) getSession()
                .createCriteria(SubjectOrganJoin.class)
                .add(Restrictions.eq("publicId", publicId))
                .add(Restrictions.eq("subjectOrganId", subjectOrganId))
                .uniqueResult();
    }

    @SuppressWarnings("unchecked") // i все буде добре...
    @Transactional // TODO Might be bottleneck, should be replaced via bulk delete (stored procedure)
    public void removeSubjectOrganJoin(Long organID, String[] publicIDs) {
        List<SubjectOrganJoin> sojs = (List<SubjectOrganJoin>) getSession()
                .createCriteria(SubjectOrganJoin.class)
                .add(Restrictions.eq("subjectOrganId", organID))
                .add(Restrictions.in("publicId", publicIDs))
                .list();

        for (SubjectOrganJoin soj : sojs)
            subjectOrganJoinDao.delete(soj);
    }
}