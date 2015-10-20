package org.activiti.test;

import static org.mockito.Mockito.mock;

public class MockUtil {
    public static <T> T createMock(Class<T> toMock) {
        return mock(toMock);
    }
}