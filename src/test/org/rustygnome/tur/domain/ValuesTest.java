package org.rustygnome.tur.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ValuesTest {

    @Test
    public void notMoreValuesThanTestedHereShouldExist() {

        // given: some arbitrary values
        Values values = new Values();

        // then: the number of values should match
        assertEquals(parameterProvider().count(), values.entrySet().size());
    }

    @ParameterizedTest
    @MethodSource("parameterProvider")
    public void valuesShouldHaveAllTestedKeys(Key key, int Index, String name) {

        // given: some arbitrary values
        Values values = Values.getTitles();

        // then: the values should hold the key
        assertNotNull(values.get(key));
    }

    @ParameterizedTest
    @MethodSource("parameterProvider")
    public void getTitles_ShouldCreateValuesWithTheExpectedTitle(Key key, int Index, String title) {

        // given: some arbitrary values
        Values values = Values.getTitles();

        // then: the value should have the expected title
        assertEquals(title, values.get(key));
    }

    @ParameterizedTest
    @MethodSource("parameterProvider")
    public void putAndGet_ShouldDoWhatYouWouldExpectFromThem(Key key, int Index, String title) {

        // given: some arbitrary values
        Values values = Values.getTitles();

        // when: we put some value
        values.put(key, "Sibelius");

        // then: we should get the same value
        assertEquals("Sibelius", values.get(key));
    }

    @Test
    public void put_ShouldActuallyOverwriteValues() {

        // given: some arbitrary values
        Values values = Values.getTitles();

        // when: we put some value
        values.put(Key.INTERESTS, "---");
        values.put(Key.NAME, "name");
        values.put(Key.DATETIME, "---");
        values.put(Key.INTERESTS, "---");
        values.put(Key.EMAIL, "---");
        values.put(Key.INTERESTS, "---");
        values.put(Key.DATETIME, "datetime");
        values.put(Key.INTERESTS, "interests");
        values.put(Key.EMAIL, "email");

        // then: the order of the values should have been preserved
        assertEquals("name", values.get(Key.NAME));
        assertEquals("email", values.get(Key.EMAIL));
        assertEquals("interests", values.get(Key.INTERESTS));
        assertEquals("datetime", values.get(Key.DATETIME));
    }

    @Test
    public void put_ShouldPreserveTheOrderOfEntries() {

        // given: some arbitrary values
        Values values = Values.getTitles();

        // when: we put some value
        values.put(Key.INTERESTS, "---");
        values.put(Key.NAME, "name");
        values.put(Key.DATETIME, "---");
        values.put(Key.INTERESTS, "---");
        values.put(Key.EMAIL, "---");
        values.put(Key.INTERESTS, "---");
        values.put(Key.DATETIME, "datetime");
        values.put(Key.INTERESTS, "interests");
        values.put(Key.EMAIL, "email");

        // then: the order of the values should have been preserved
        String[] strings = values.toArray();
        assertEquals("datetime", strings[0]);
        assertEquals("name", strings[1]);
        assertEquals("email", strings[2]);
        assertEquals("interests", strings[3]);
    }

    @Test
    public void toArray_ShouldReturnAnArrayHoldingValues() {

        // given: some arbitrary values
        Values values = Values.getTitles();

        // when: we get the values as array
        String[] strings = values.toArray();

        // then: the array should hold the expected strings
        assertEquals("Zeitpunkt", strings[0]);
        assertEquals("Name", strings[1]);
        assertEquals("E-Mail", strings[2]);
        assertEquals("Interessen", strings[3]);
    }

    @Test
    public void toString_ShouldReturnTheExpectedStringRepresentation() {

        // given: some arbitrary values
        Values values = Values.getTitles();

        // when: we expect a specific representation of the values
        String string =
                "Zeitpunkt: Zeitpunkt\n" +
                "Name: Name\n" +
                "E-Mail: E-Mail\n" +
                "Interessen: Interessen";

        // then: the string should represent the values as expected
        assertEquals(string, values.toString());
    }

    @Test
    public void entrySet_ShouldReturnAFullEntrySet() {

        // given: some arbitrary values
        Values values = Values.getTitles();

        // when: we get an entrySet
        List<Values.Entry> entries = values.entrySet();

        // then: all values should be contained
        Iterator<Values.Entry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Values.Entry entry = iterator.next();
            assertEquals(entry.getKey().getTitle(), entry.getValue());
        }
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(
                Arguments.of(Key.DATETIME, 0, "Zeitpunkt"),
                Arguments.of(Key.NAME, 1, "Name"),
                Arguments.of(Key.EMAIL, 2, "E-Mail"),
                Arguments.of(Key.INTERESTS, 3, "Interessen"));
    }
}
