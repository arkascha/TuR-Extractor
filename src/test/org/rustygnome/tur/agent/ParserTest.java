package org.rustygnome.tur.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Key;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factory;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ParserTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @Test
    public void creatingAnInstance_ShouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // when: getting an instance
        Parser instance = Parser.getInstance(mock(Command.class));

        // then: it should be an instance
        assertEquals(Parser.class, instance.getClass());
    }

    @Test
    public void getInstance_shouldUseTheFactory()
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Parser mockedParser = mock(Parser.class);
        Factory<Parser> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact(any(Command.class))).thenReturn(mockedParser);
        Factory.setInstance(Parser.class, mockedFactory);

        // when: getInstance() is called
        Parser parser = Parser.getInstance(mock(Command.class));

        // then: the factories createArtefact method should get called
        verify(mockedFactory, times(1)).createArtifact(any(Command.class));

        // and: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedParser, parser);
    }

    @Test
    public void extract_shouldThrowAnExceptionWithoutMessageToExtractFrom()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // and: the parser
        Parser parser = Parser.getFactory().createArtifact(mock(Command.class));

        // when: the extraction is performed
        Values values = parser.parse(null);

        // then: a placeholder output should appear in STDOUT
        assertNull(values);
    }

    @Test
    public void extract_shouldExtractValuesAsExpected()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // given: a message as expected
        final String message =
                "\n Name, Vorname: In die Luft, Hans  " +
                "\nE-Mail: hans@mailinator.com " +
                "\nMeine Hauptinteressen sind ....: Freunde am Miteinander! " +
                "\nNutzer hat die Datenschutzerkl=C3=A4rung akzeptiert. " +
                "\nDatum/Uhrzeit: 1999-09-19 09:19:29 CET \n";

        // and: the parser
        Parser parser = Parser.getFactory().createArtifact(mock(Command.class));

        // when: the extraction is performed
        Values values = parser.parse(message);

        // then: the values should be the expected ones
        assertEquals("In die Luft, Hans", values.get(Key.NAME));
        assertEquals("hans@mailinator.com", values.get(Key.EMAIL));
        assertEquals("Freunde am Miteinander!", values.get(Key.INTERESTS));
        assertEquals("1999-09-19 09:19:29", values.get(Key.DATETIME));
    }

    @Test
    public void extract_shouldThroughAnExceptionIfMessageStructureDoesNotMatch()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // given: a message with unexpected structure
        final String message =
                "\n Name, Beruf: Martin, Clown " +
                "\nFax-Nummer: 08/15 " +
                "\nHobbies\\s+\\.+: Warten " +
                "\nDatum/Uhrzeit: vorgestern \n";

        // and: the parser
        Parser parser = Parser.getFactory().createArtifact(mock(Command.class));

        // when: the extraction is performed
        // then: an exception should get raised
        assertThrows(RuntimeException.class, () -> {
            parser.parse(message);
        });
    }

    @Test
    public void extract_shouldFormatDatesAsExpected()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // given: a message as expected
        final String message =
                "\n Name, Vorname: NAME  " +
                        "\nE-Mail: ADDRESS " +
                        "\nMeine Hauptinteressen sind ....: INTEREST " +
                        "\nNutzer hat die Datenschutzerkl=C3=A4rung akzeptiert. " +
                        "\nDatum/Uhrzeit: 1999-09= =2D19 09:19:29 CET \n";

        // and: the parser
        Parser parser = Parser.getFactory().createArtifact(mock(Command.class));

        // when: the extraction is performed
        Values values = parser.parse(message);

        // then: the datetime value should have the expected format
        assertEquals("19.09.1999 09:19", values.get(Key.DATETIME));
    }

    @Test
    public void extract_shouldPreserveDatesIfInvalid()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // given: a message as expected
        final String message =
                "\n Name, Vorname: NAME  " +
                        "\nE-Mail: ADDRESS " +
                        "\nMeine Hauptinteressen sind ....: INTEREST " +
                        "\nNutzer hat die Datenschutzerkl=C3=A4rung akzeptiert. " +
                        "\nDatum/Uhrzeit: 1999-A 16 GORKY CET \n";

        // and: the parser
        Parser parser = Parser.getFactory().createArtifact(mock(Command.class));

        // when: the extraction is performed
        Values values = parser.parse(message);

        // then: the datetime value should have the expected format
        assertEquals("1999-A 16 GORKY", values.get(Key.DATETIME));
    }
}
