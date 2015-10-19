package org.wf.dp.dniprorada.base.dao;

import org.wf.dp.dniprorada.base.model.AccessServiceLoginRight;

import java.util.List;

/**
 * User: goodg_000
 * Date: 06.10.2015
 * Time: 22:31
 */
public interface AccessServiceLoginRightDao extends EntityDao<AccessServiceLoginRight> {

    AccessServiceLoginRight getAccessServiceLoginRight(String sLogin, String sService);

    List<String> getAccessibleServices(String sLogin);
}
