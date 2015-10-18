package org.wf.dp.dniprorada.dao;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Utility class which creates random value of given type.
 */
public final class RandomUtils {
    private static final int RANDOM_STRING_SIZE = 10;
    private static final int DATE_WINDOW_DAYS = 30;

    private Random random;
    private Randomizer<Byte> byteRandomizer = new Randomizer<Byte>() {
        public Byte random(Class<Byte> givenType) {
            return (byte) random.nextInt();
        }
    };
    private Randomizer<Short> shortRandomizer = new Randomizer<Short>() {
        public Short random(Class<Short> givenType) {
            return (short) random.nextInt();
        }
    };
    private Randomizer<Integer> integerRandomizer = new Randomizer<Integer>() {
        public Integer random(Class<Integer> givenType) {
            return random.nextInt();
        }
    };
    private Randomizer<Long> longRandomizer = new Randomizer<Long>() {
        public Long random(Class<Long> givenType) {
            return random.nextLong();
        }
    };
    private Randomizer<Float> floatRandomizer = new Randomizer<Float>() {
        public Float random(Class<Float> givenType) {
            return random.nextFloat();
        }
    };
    private Randomizer<Double> doubleRandomizer = new Randomizer<Double>() {
        public Double random(Class<Double> givenType) {
            return random.nextDouble();
        }
    };
    private Randomizer<Boolean> booleanRandomizer = new Randomizer<Boolean>() {
        public Boolean random(Class<Boolean> givenType) {
            return random.nextBoolean();
        }
    };
    private Randomizer<Character> characterRandomizer = new Randomizer<Character>() {
        public Character random(Class<Character> givenType) {
            return (char) random.nextInt();
        }
    };
    private Randomizer<String> stringRandomizer = new Randomizer<String>() {
        public String random(Class<String> givenType) {
            return RandomStringUtils.random(RANDOM_STRING_SIZE, true, false);
        }
    };
    private Randomizer<DateTime> dateTimeRandomizer = new Randomizer<DateTime>() {
        public DateTime random(Class<DateTime> givenType) {
            return DateTime.now().minusDays(random.nextInt(DATE_WINDOW_DAYS) - random.nextInt(DATE_WINDOW_DAYS));
        }
    };
    private Randomizer<Enum> enumRandomizer = new Randomizer<Enum>() {
        public Enum random(Class<Enum> givenType) {
            Enum[] enumValues = givenType.getEnumConstants();
            return enumValues[random.nextInt(enumValues.length)];
        }
    };
    private Randomizer<Collection> collectionRandomizer = new Randomizer<Collection>() {
        @Override
        public Collection random(Class<Collection> givenType) {
            return Lists.newArrayList();
        }
    };

    public RandomUtils() {
        random = new Random();
    }

    public RandomUtils(long seed) {
        random = new Random(seed);
    }

    public Object[] getRandomParams(Class<?>[] parameterTypes) {
        List<Object> randomParams = Lists.newLinkedList();

        for (Class<?> parameterType : parameterTypes) {
            randomParams.add(getRandomValueOf(parameterType));
        }

        return Iterables.toArray(randomParams, Object.class);
    }

    /**
     * Creates random value of given type
     *
     * @param type creates random value of given type
     * @return random value or default value see {@link DefaultRandomizer} for details
     * @see RandomUtils#getRandomizer(Class)  - supported types
     */
    public Object getRandomValueOf(Class<?> type) {
        return getRandomizer(type).random(type);
    }

    private Randomizer getRandomizer(Class<?> type) {
        Randomizer randomizer = null;

        if (in(type, byte.class, Byte.class)) {
            randomizer = byteRandomizer;
        } else if (in(type, short.class, Short.class)) {
            randomizer = shortRandomizer;
        } else if (in(type, int.class, Integer.class)) {
            randomizer = integerRandomizer;
        } else if (in(type, long.class, Long.class)) {
            randomizer = longRandomizer;
        } else if (in(type, float.class, Float.class)) {
            randomizer = floatRandomizer;
        } else if (in(type, double.class, Double.class)) {
            randomizer = doubleRandomizer;
        } else if (in(type, boolean.class, Boolean.class)) {
            randomizer = booleanRandomizer;
        } else if (in(type, char.class, Character.class)) {
            randomizer = characterRandomizer;
        } else if (type == String.class) {
            randomizer = stringRandomizer;
        } else if (type == DateTime.class) {
            randomizer = dateTimeRandomizer;
        } else if (type.isEnum()) {
            randomizer = enumRandomizer;
        } else if (type.isInstance(Collection.class)) {
            randomizer = collectionRandomizer;
        }

        return randomizer == null ? new DefaultRandomizer(this) : randomizer;
    }

    private boolean in(Class<?> type, Class<?>... types) {
        return ArrayUtils.contains(types, type);
    }

    public interface Randomizer<T> {
        T random(Class<T> givenType);
    }

}
