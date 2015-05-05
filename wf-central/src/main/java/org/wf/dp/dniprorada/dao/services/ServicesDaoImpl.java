package org.wf.dp.dniprorada.dao.services;

import org.wf.dp.dniprorada.dao.AbstractEntityDao;
import org.wf.dp.dniprorada.model.Service;

/**
 * User: goodg_000
 * Date: 05.05.2015
 * Time: 22:30
 */
public class ServicesDaoImpl extends AbstractEntityDao<Service> implements ServicesDao {

   public ServicesDaoImpl() {
      super(Service.class);
   }
}
