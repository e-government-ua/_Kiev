package org.wf.dp.dniprorada.base.dao;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Creates object with fields populated with random values.
 */
public class DefaultRandomizer implements RandomUtils.Randomizer {
    private static final Log LOG = LogFactory.getLog(DefaultRandomizer.class);

    private RandomUtils randomUtils;

    public DefaultRandomizer(RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    @Override
    public Object random(Class givenType) {
        Constructor constructor = getAppropriateConstructor(givenType);

        Object instance = newInstanceWithRandomParams(constructor);

        return populateInstanceWithRandomValues(instance);
    }

    private Object populateInstanceWithRandomValues(Object instance) {
        Method[] setters = getSetters(instance);

        for (Method setter : setters) {
            Object[] randomParams = randomUtils.getRandomParams(setter.getParameterTypes());
            invokeMethod(instance, setter, randomParams);
        }

        return instance;
    }

    private Object invokeMethod(Object instance, Method setter, Object[] randomParams) {
        try {
            return setter.invoke(instance, randomParams);
        } catch (ReflectiveOperationException e) {
            //should not get here
            LOG.error("Could not invoke setter", e);
            throw new IllegalStateException(e);
        }
    }

    private Method[] getSetters(Object instance) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(instance);

        return FluentIterable.of(propertyDescriptors)
                .transform(new Function<PropertyDescriptor, Method>() {
                    @Override
                    public Method apply(PropertyDescriptor input) {
                        return input.getWriteMethod();
                    }
                })
                .filter(Predicates.notNull())
                .toArray(Method.class);
    }

    protected Object newInstanceWithRandomParams(Constructor constructor) {
        Object[] randomParams = randomUtils.getRandomParams(constructor.getParameterTypes());
        return newInstance(constructor, randomParams);
    }

    protected Constructor getAppropriateConstructor(Class givenType) {
        Constructor[] declaredConstructors = givenType.getDeclaredConstructors();

        return findDefaultOrTheBiggestConstructor(declaredConstructors);
    }

    protected Object newInstance(Constructor constructor, Object[] randomParams) {
        try {
            return constructor.newInstance(randomParams);
        } catch (ReflectiveOperationException e) {
            //should not get here
            LOG.error("Could not create new instance of given type", e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * Finds constructor with no parameters or constructor with the biggest number of parameters.
     *
     * @param declaredConstructors
     * @return
     */
    private Constructor findDefaultOrTheBiggestConstructor(Constructor[] declaredConstructors) {
        Pair<Constructor, Integer> candidate = null;

        for (Constructor constructor : declaredConstructors) {
            Class[] parameterTypes = constructor.getParameterTypes();

            if (ArrayUtils.isEmpty(parameterTypes)) {
                return constructor;
            } else {
                if (candidate == null) {
                    candidate = Pair.of(constructor, parameterTypes.length);
                    continue;
                }

                if (candidate.getValue() < parameterTypes.length) {
                    candidate = Pair.of(constructor, parameterTypes.length);
                }
            }
        }

        return candidate.getKey();
    }
}
