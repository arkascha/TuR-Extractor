package org.rustygnome.tur.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyTest {

    @Test
    public void notMoreKeysThanTestedHereShouldExist() {
        assertEquals(parameterProvider().count(), Key.values().length);
    }

    @ParameterizedTest
    @MethodSource("parameterProvider")
    public void keysShouldHaveTheExpectedIndex(Key key, int index, String name) {
        assertEquals(index, key.getIndex());
    }

    @ParameterizedTest
    @MethodSource("parameterProvider")
    public void keysShouldHaveTheExpectedName(Key key, int index, String name) {
        assertEquals(name, key.getTitle());
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(
            Arguments.of(Key.DATETIME, 0, "Zeitpunkt"),
            Arguments.of(Key.NAME, 1, "Name"),
            Arguments.of(Key.EMAIL, 2, "E-Mail"),
            Arguments.of(Key.INTERESTS, 3, "Interessen"));
    }
}
