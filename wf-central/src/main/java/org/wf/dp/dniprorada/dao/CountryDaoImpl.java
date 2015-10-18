package org.wf.dp.dniprorada.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.EntityNotFoundException;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.Country;

import java.util.List;

@Repository
public class CountryDaoImpl extends GenericEntityDao<Country>
        implements CountryDao {

    private static final Logger log = Logger.getLogger(CountryDaoImpl.class);

    protected CountryDaoImpl() {
        super(Country.class);
    }

    //return false if all args are null
    //otherwise set params to criteria
    //note: length arrays must be the same
    //TODO: можно вынести в BaseEntityDao
    private boolean setRestrictions(Criteria criteria, String[] columns, Object[] values) {
        boolean allAreNull = true;
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            if (value != null) {
                criteria.add(Restrictions.eq(columns[i], value));
                allAreNull = false;
            }
        }
        return !allAreNull;
    }

    @SuppressWarnings("unchecked")
    @Override //отдает массив объектов сущности, подпадающих под критерии
    // later edit or delete this method
    public List<Country> getAll(Long nID_ua, String sID_two, String sID_three,
            String sNameShort_ua, String sNameShort_en) {

        Criteria criteria = getSession().createCriteria(Country.class);
        boolean setRestrictions = setRestrictions(criteria,
                new String[] { "nID_UA", "sID_Two", "sID_Three", "sNameShort_UA", "sNameShort_EN" },
                new Object[] { nID_ua, sID_two, sID_three, sNameShort_ua, sNameShort_en });

        if (!setRestrictions)
            throw new IllegalArgumentException("all args are null!");

        return (List<Country>) criteria.list();
    }

    @Override   //апдейтит элемент(если задан один из уникальных-ключей)
    //или вставляет (если не задан nID), и отдает экземпляр нового объекта
    public Country setCountry(Long nID, Long nID_ua, String sID_two, String sID_three,
            String sNameShort_ua, String sNameShort_en, String sReference_localISO) {

        Country country = getByKey(nID, nID_ua, sID_two, sID_three);

        if (country == null) {
            if (nID == null) {
                country = new Country();
            } else {
                throw new EntityNotFoundException("Record not found!");
            }
        }
        //set values (if they are not null and not the same
        if (nID_ua != null && country.getnID_UA() != nID_ua)
            country.setnID_UA(nID_ua);
        if (sID_two != null && !sID_two.equals(country.getsID_Two()))
            country.setsID_Two(sID_two);
        if (sID_three != null && !sID_three.equals(country.getsID_Three()))
            country.setsID_Three(sID_three);
        if (sNameShort_ua != null && !sNameShort_ua.equals(country.getsNameShort_UA()))
            country.setsNameShort_UA(sNameShort_ua);
        if (sNameShort_en != null && !sNameShort_en.equals(country.getsNameShort_EN()))
            country.setsNameShort_EN(sNameShort_en);
        if (sReference_localISO != null && !sReference_localISO.equals(country.getsReference_LocalISO()))
            country.setsReference_LocalISO(sReference_localISO);

        country = saveOrUpdate(country);
        log.info("country " + country + "is updated");
        return country;
    }

    //return true if all args are null
    private boolean areAllArgsNull(Object... args) {
        boolean result = true;
        for (Object o : args) {
            if (o != null) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override //удаляет элемент(по одному из уникальных-ключей)
    public void removeByKey(Long nID, Long nID_ua, String sID_two, String sID_three) {
        Country country = getByKey(nID, nID_ua, sID_two, sID_three);
        if (country == null) {
            throw new EntityNotFoundException("Record not found!");
        } else {
            delete(country);
            log.info("country " + country + "is deleted");
        }
    }

    @Override
    public Country getByKey(Long nID, Long nID_ua, String sID_two, String sID_three) {
        if (nID != null) {
            return findById(nID).orNull();
        } else if (nID_ua != null) {
            return findBy("nID_UA", nID_ua).orNull();
        } else if (sID_two != null) {
            return findBy("sID_Two", sID_two).orNull();
        } else if (sID_three != null) {
            return findBy("sID_Three", sID_three).orNull();
        } else
            throw new IllegalArgumentException("All args are null!");
    }
}
