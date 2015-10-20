package org.wf.dp.dniprorada.base.util.caching;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.wf.dp.dniprorada.base.util.SerializationUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * User: goodg_000
 * Date: 16.06.2015
 * Time: 21:35
 */
public class MethodCacheInterceptor implements MethodInterceptor, InitializingBean {
    private static final Log logger = LogFactory.getLog(MethodCacheInterceptor.class);

    private Cache cache;

    /**
     * sets cache name to be used
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * Checks if required attributes are provided.
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cache, "A cache is required. Use setCache(Cache) to provide one.");
    }

    /**
     * main method
     * caches method result if method is configured for caching
     * method results must be serializable
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String targetName = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();
        Object[] arguments = invocation.getArguments();
        Object result;

        logger.debug("looking for method result in cache");
        String cacheKey = getCacheKey(targetName, methodName, arguments);
        Element element = cache.get(cacheKey);
        if (element == null) {
            //call target/sub-interceptor
            logger.debug("calling intercepted method");
            result = invocation.proceed();

            //cache method result
            logger.debug("caching result");

            Serializable preparedResult = null;
            if (result instanceof Serializable) {
                preparedResult = (Serializable) result;
            } else {
                preparedResult = SerializationUtil.getByteArrayFromObject(result);
            }

            element = new Element(cacheKey, preparedResult);
            cache.put(element);

            return result;
        }

        result = element.getObjectValue();
        if (result instanceof byte[]) {
            result = SerializationUtil.getByteArrayFromObject(result);
        }

        return result;
    }

    private void validateMethodExist(Class beanClass, String methodName) {
        boolean exist = false;

        for (Method m : beanClass.getMethods()) {
            if (m.getName().equals(methodName)) {
                exist = true;
                break;
            }
        }

        Assert.isTrue(exist, String.format("Method %s of class %s is not exist!", methodName, beanClass.getClass()));
    }

    public void clearCacheForMethod(Class beanClass, String methodName, Object... args) {
        validateMethodExist(beanClass, methodName);

        String targetName = beanClass.getName();
        String partOfKey = getCacheKey(targetName, methodName, args);

        List<String> keysToDelete = new ArrayList<>();
        for (Object key : cache.getKeys()) {
            String stringKey = (String) key;
            if (stringKey.startsWith(partOfKey)) {
                keysToDelete.add(stringKey);
            }
        }

        if (!keysToDelete.isEmpty()) {
            cache.removeAll(keysToDelete);
        }
    }

    /**
     * creates cache key: targetName.methodName.argument0.argument1...
     */
    private String getCacheKey(String targetName,
            String methodName,
            Object[] arguments) {
        StringBuilder sb = new StringBuilder();
        sb.append(targetName).append(".").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (Object argument : arguments) {
                sb.append(".").append(argument);
            }
        }

        return sb.toString();
    }
}