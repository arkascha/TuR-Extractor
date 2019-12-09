package org.rustygnome.tur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParserTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @Test
    public void getInstance_ShouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException {

        // when: getting an instance
        Parser instance = Parser.getInstance();

        // then: it should be an instance
        assertEquals(Parser.class, instance.getClass());
    }

    @Test
    public void getInstance_ShouldUseTheFactory()
            throws InstantiationException, IllegalAccessException {

        // given: a mocked factory
        Parser mockedParser = mock(Parser.class);
        Factory<Parser> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtefact()).thenReturn(mockedParser);
        Factory.setInstance(Parser.class, mockedFactory);

        // when: getInstance() is called
        Parser parser = Parser.getInstance();

        // then: the returned artefact should be the one created by the mocked factory
        assertEquals(mockedParser, parser);
    }

    @Test
    public void extract_ShouldThrowAnExceptionWithoutMessageToExtractFrom()
            throws IllegalAccessException, InstantiationException {

        // and: the parser
        Parser parser = Parser.getInstance();

        // when: the extraction is performed
        // then: an exception should get raised
        assertThrows(RuntimeException.class, () -> {
            parser.extract(null);
        });
    }

    @Test
    public void extract_ShouldExtractValuesAsExpected()
            throws IllegalAccessException, InstantiationException {

        // given: a message as expected
        final String message =
                "\n Name, Vorname: In die Luft, Hans  " +
                "\nE-Mail: hans@mailinator.com " +
                "\nMeine Hauptinteressen sind ....: Freunde am Miteinander! " +
                "\nNutzer hat die Datenschutzerkl=C3=A4rung akzeptiert. " +
                "\nDatum/Uhrzeit: 1999-09-19 09:19:29 CET \n";

        // and: the parser
        Parser parser = Parser.getInstance();

        // when: the extraction is performed
        Values values = parser.extract(message);

        // then: the values should be the expected ones
        assertEquals("In die Luft, Hans", values.get(Key.NAME));
        assertEquals("hans@mailinator.com", values.get(Key.EMAIL));
        assertEquals("Freunde am Miteinander!", values.get(Key.INTERESTS));
        assertEquals("1999-09-19 09:19:29", values.get(Key.DATETIME));
    }

    @Test
    public void extract_ShouldThroughAnExceptionIfMessageStructureDoesNotMatch()
            throws IllegalAccessException, InstantiationException {

        // given: a message with unexpected structure
        final String message =
                "\n Name, Beruf: Martin, Clown " +
                "\nFax-Nummer: 08/15 " +
                "\nHobbies\\s+\\.+: Warten " +
                "\nDatum/Uhrzeit: übergestern \n";

        // and: the parser
        Parser parser = Parser.getInstance();

        // when: the extraction is performed
        // then: an exception should get raised
        assertThrows(RuntimeException.class, () -> {
            parser.extract(message);
        });
    }

    @Test
    public void extract_ShouldFormatDatesAsExpected()
            throws IllegalAccessException, InstantiationException {

        // given: a message as expected
        final String message =
                "\n Name, Vorname: NAME  " +
                        "\nE-Mail: ADDRESS " +
                        "\nMeine Hauptinteressen sind ....: INTEREST " +
                        "\nNutzer hat die Datenschutzerkl=C3=A4rung akzeptiert. " +
                        "\nDatum/Uhrzeit: 1999-09= =2D19 09:19:29 CET \n";

        // and: the parser
        Parser parser = Parser.getInstance();

        // when: the extraction is performed
        Values values = parser.extract(message);

        // then: the datetime value should have the expected format
        assertEquals("19.09.1999 09:19", values.get(Key.DATETIME));
    }

    @Test
    public void extract_ShouldPreserveDatesIfInvalid()
            throws IllegalAccessException, InstantiationException {

        // given: a message as expected
        final String message =
                "\n Name, Vorname: NAME  " +
                        "\nE-Mail: ADDRESS " +
                        "\nMeine Hauptinteressen sind ....: INTEREST " +
                        "\nNutzer hat die Datenschutzerkl=C3=A4rung akzeptiert. " +
                        "\nDatum/Uhrzeit: 1999-A 16 GORKY CET \n";

        // and: the parser
        Parser parser = Parser.getInstance();

        // when: the extraction is performed
        Values values = parser.extract(message);

        // then: the datetime value should have the expected format
        assertEquals("1999-A 16 GORKY", values.get(Key.DATETIME));
    }
}
