package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.Country;

import java.util.List;

public interface CountryDao extends EntityDao<Country> {

    public List<Country> getAll(Long nID_ua, String sID_two, String sID_three,
            String sNameShort_ua, String sNameShort_en);

    public Country setCountry(Long nID, Long nID_ua, String sID_two, String sID_three,
            String sNameShort_ua, String sNameShort_en, String sReference_localISO);

    public void removeByKey(Long nID, Long nID_ua, String sID_two, String sID_three);

    public Country getByKey(Long nID, Long nID_ua, String sID_two, String sID_three);
}
