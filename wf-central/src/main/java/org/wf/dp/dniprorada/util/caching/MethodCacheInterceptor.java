package org.wf.dp.dniprorada.util.caching;

import java.io.ObjectInputStream;
import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.wf.dp.dniprorada.util.SerializationUtil;

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
      String targetName  = invocation.getThis().getClass().getName();
      String methodName  = invocation.getMethod().getName();
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
            preparedResult = (Serializable)result;
         }
         else {
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