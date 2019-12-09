package org.rustygnome.tur;

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
    public void keysShouldHaveTheExpectedName(Key key, String name) {
        assertEquals(name, key.toString());
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(
            Arguments.of(Key.NAME, "Name"),
            Arguments.of(Key.EMAIL, "E-Mail"),
            Arguments.of(Key.INTERESTS, "Interessen"),
            Arguments.of(Key.DATETIME, "Zeitpunkt"));
    }
}
