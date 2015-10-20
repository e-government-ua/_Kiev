package org.wf.dp.dniprorada.dao;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * This test takes all {@link EntityDao} DAOs and executes all select(get, search, find) methods with random parameters.
 * The purpose of this test is to make sure all queries were written correctly.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/testContext.xml")
public class EntityDaoQueriesTest {
    private static final Log LOG = LogFactory.getLog(EntityDaoQueriesTest.class);

    private static final String[] QUERY_METHOD_PREFIXES = { "get", "search", "find" };
    private static final String LOG_SEPARATOR_LINE = StringUtils.repeat("=", 100);
    private static final String ALL_METHODS_SYMBOL = "*";

    @Value("${repeatNumber}")
    private int repeatNumber;

    @Value("${seed}")
    private Long seed;

    @Value("${exclude}")
    private String[] exclusions;
    private Multimap<String, String> excludedDaos = HashMultimap.create();

    private RandomUtils randomUtils;

    @Autowired
    private ApplicationContext applicationContext;

    private Set<String> failedMethods = Sets.newLinkedHashSet();

    @Before
    public void setUp() throws Exception {
        randomUtils = seed != null ? new RandomUtils(seed) : new RandomUtils();

        for (String exclusion : exclusions) {
            String className = StringUtils.substringBeforeLast(exclusion, ".");
            String methodName = StringUtils.substringAfterLast(exclusion, ".");

            excludedDaos.put(className, methodName);
        }
    }

    @Test
    @Transactional(readOnly = true)
    public void shouldFindAllDaoAndExecuteEachQueryMethod() throws Exception {
        LOG.info(LOG_SEPARATOR_LINE);
        LOG.info("Obtaining DAO for test");

        Map<String, EntityDao> entityDaos = applicationContext.getBeansOfType(EntityDao.class);

        LOG.info(LOG_SEPARATOR_LINE);
        LOG.info(String.format("Found %s DAOs : %s", entityDaos.size(), entityDaos));

        for (Map.Entry<String, EntityDao> entityDaoEntry : entityDaos.entrySet()) {
            EntityDao testedDao = entityDaoEntry.getValue();
            Class<? extends EntityDao> testedDaoType = getType(testedDao);
            String testedDaoClassName = testedDaoType.getName();

            if (testedDaoType == GenericEntityDao.class) {
                LOG.info(LOG_SEPARATOR_LINE);
                LOG.info(String.format("DAO %s is skipped from test, because it's %s",
                        entityDaoEntry.getKey(), GenericEntityDao.class.getName()));
                continue;
            }

            if (isDaoExcluded(testedDaoClassName)) {
                LOG.info(LOG_SEPARATOR_LINE);
                LOG.info(String.format("DAO %s is excluded from test", testedDaoClassName));
                continue;
            }

            LOG.info(LOG_SEPARATOR_LINE);
            LOG.info(String.format("Run test for %s", testedDaoClassName));

            testDaoMethods(testedDao);
        }

        if (CollectionUtils.isNotEmpty(failedMethods)) {
            fail(String.format("Test failed (see stacktrace above for details):\n%s",
                    Joiner.on('\n').join(failedMethods)));
        } else {
            LOG.info(LOG_SEPARATOR_LINE);
            LOG.info(String.format("Testing of %s DAOs completed successfully", entityDaos.size()));
            LOG.info(LOG_SEPARATOR_LINE);
        }
    }

    protected boolean isDaoExcluded(String testedDaoClassName) {
        Collection<String> methodNames = excludedDaos.get(testedDaoClassName);
        return methodNames.contains(ALL_METHODS_SYMBOL);
    }

    private void testDaoMethods(final EntityDao testedDao) {
        final Class<? extends EntityDao> testedDaoClass = getType(testedDao);

        ReflectionUtils.doWithMethods(testedDaoClass, new ReflectionUtils.MethodCallback() {
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                LOG.info(LOG_SEPARATOR_LINE);
                LOG.info(String.format("Run test for %s method %s times", method, repeatNumber));

                for (int i = 0; i < repeatNumber; i++) {
                    LOG.info(String.format("%s execution of %s method", i + 1, method));
                    executeMethodWithRandomParams(testedDao, method);
                }
            }
        }, new ReflectionUtils.MethodFilter() {
            public boolean matches(Method method) {
                if (method.getDeclaringClass() != testedDaoClass || Modifier.isPrivate(method.getModifiers())) {
                    return false;
                }

                if (excludeMethod(method, testedDaoClass)) {
                    LOG.info(String.format("%s method is excluded from test", method));
                    return false;
                }

                return StringUtils.startsWithAny(method.getName(), QUERY_METHOD_PREFIXES);
            }
        });
    }

    protected boolean excludeMethod(Method method, Class<? extends EntityDao> testedDaoClass) {
        return excludedDaos.get(testedDaoClass.getName()).contains(method.getName());
    }

    private Class<? extends EntityDao> getType(EntityDao testedDao) {
        return (Class<? extends EntityDao>) AopUtils.getTargetClass(testedDao);
    }

    private void executeMethodWithRandomParams(EntityDao testedDao, Method testedMethod) {
        Object[] randomParams = getRandomParams(testedMethod);
        LOG.info(String.format("Generated params for method %s: %s", testedMethod, Arrays.toString(randomParams)));

        try {
            ReflectionUtils.invokeMethod(testedMethod, getRealObject(testedDao), randomParams);
        } catch (HibernateException e) {
            handleTestedMethodException(testedMethod, randomParams, e);
        }
    }

    private Object getRealObject(EntityDao testedDao) {
        if (AopUtils.isAopProxy(testedDao)) {
            Advised advisedTestedDao = (Advised) testedDao;
            try {
                return advisedTestedDao.getTargetSource().getTarget();
            } catch (Exception e) {
                throw new IllegalStateException("Should not get here");
            }
        }

        return testedDao;
    }

    private void handleTestedMethodException(Method testedMethod, Object[] randomParams, HibernateException e) {
        LOG.error("Method invocation failed!", e);
        failedMethods
                .add(String.format("Failed method: %s with params: %s", testedMethod, Arrays.toString(randomParams)));
    }

    private Object[] getRandomParams(Method testedMethod) {
        Class<?>[] parameterTypes = testedMethod.getParameterTypes();

        return randomUtils.getRandomParams(parameterTypes);
    }
}
