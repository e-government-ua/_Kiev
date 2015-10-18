package org.wf.dp.dniprorada.base.service.flow.propertyHandler;

import java.util.List;

/**
 * User: goodg_000
 * Date: 29.06.2015
 * Time: 18:56
 */
public interface FlowPropertyHandler<T> {

    Class<T> getTargetObjectClass();

    List<T> generateObjects(String sData);

}
