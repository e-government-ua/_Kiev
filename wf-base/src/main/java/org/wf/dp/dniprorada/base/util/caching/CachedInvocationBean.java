package org.wf.dp.dniprorada.base.util.caching;

import org.apache.commons.lang3.StringUtils;

/**
 * User: goodg_000
 * Date: 16.06.2015
 * Time: 22:21
 */
public class CachedInvocationBean {

    @EnableCaching
    public <T> T invokeUsingCache(Callback<T> callback) {
        return callback.execute();
    }

    public static abstract class Callback<T> {
        private Object[] cacheKeyParts;

        public Callback(Object... cacheKeyParts) {
            this.cacheKeyParts = cacheKeyParts;
        }

        public abstract T execute();

        @Override
        public String toString() {
            return StringUtils.join(cacheKeyParts, ",");
        }
    }
}
