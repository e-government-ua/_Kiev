package org.wf.dp.dniprorada.base.service.access;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.wf.dp.dniprorada.base.dao.AccessServiceLoginRightDao;
import org.wf.dp.dniprorada.base.model.AccessServiceLoginRight;

import java.util.List;

/**
 * User: goodg_000
 * Date: 06.10.2015
 * Time: 21:47
 */
@Service
public class AccessService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private AccessServiceLoginRightDao accessServiceLoginRightDao;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public boolean hasAccessToService(String sLogin, String sService, String sData)
            throws HandlerBeanValidationException {

        boolean res = false;

        AccessServiceLoginRight access = accessServiceLoginRightDao.getAccessServiceLoginRight(sLogin, sService);
        if (access != null) {

            String handlerBeanName = access.getsHandlerBean();
            if (handlerBeanName != null) {
                AccessServiceLoginRightHandler handler = getHandlerBean(handlerBeanName);
                res = handler.hasAccessToService(sData);
            } else {
                res = true;
            }

        }

        return res;
    }

    public void saveOrUpdateAccessServiceLoginRight(String sLogin, String sService, String sHandlerBean)
            throws HandlerBeanValidationException {
        AccessServiceLoginRight access = accessServiceLoginRightDao.getAccessServiceLoginRight(sLogin, sService);
        if (access == null) {
            access = new AccessServiceLoginRight();
        }

        if (sHandlerBean != null) {
            getHandlerBean(sHandlerBean);
        }

        access.setsLogin(sLogin);
        access.setsService(sService);
        access.setsHandlerBean(sHandlerBean);

        accessServiceLoginRightDao.saveOrUpdate(access);
    }

    public boolean removeAccessServiceLoginRight(String sLogin, String sService) {
        boolean removed = false;
        AccessServiceLoginRight access = accessServiceLoginRightDao.getAccessServiceLoginRight(sLogin, sService);

        if (access != null) {
            accessServiceLoginRightDao.delete(access);
            removed = true;
        }

        return removed;
    }

    private AccessServiceLoginRightHandler getHandlerBean(String sHandlerBean) throws HandlerBeanValidationException {
        Object bean = applicationContext.getBean(sHandlerBean);
        if (bean == null) {
            throw new HandlerBeanValidationException(String.format(
                    "AccessServiceLoginRightHandler bean with name '%s' is not found!", sHandlerBean));
        } else if (!(bean instanceof AccessServiceLoginRightHandler)) {
            throw new HandlerBeanValidationException(String.format(
                    "Bean with name '%s' should implement interface %s, but actual class is %s", sHandlerBean,
                    AccessServiceLoginRightHandler.class, bean.getClass()));
        }

        return (AccessServiceLoginRightHandler) bean;
    }

    public List<String> getAccessibleServices(String sLogin) {
        return accessServiceLoginRightDao.getAccessibleServices(sLogin);
    }
}
