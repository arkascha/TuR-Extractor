package org.rustygnome.tur.agent;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.factory.Factory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReaderTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @Test
    public void creatingAnInstance_shouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // when: getting an instance
        Reader instance = Reader.getInstance(mock(Command.class));

        // then: it should be an instance
        assertEquals(Reader.class, instance.getClass());
    }

    @Test
    public void getInstance_shouldUseTheFactory()
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Reader mockedReader = mock(Reader.class);
        Factory<Reader> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact(any(Command.class))).thenReturn(mockedReader);
        Factory.setInstance(Reader.class, mockedFactory);

        // when: getInstance() is called
        Reader reader = Reader.getInstance(mock(Command.class));

        // then: the factories createArtefact method should get called
        verify(mockedFactory, times(1)).createArtifact(any(Command.class));

        // and: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedReader, reader);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Das Pferd frisst keinen Kartoffelsalat.", ""})
    public void readInput_shouldReadTheInput(String input)
            throws IllegalAccessException, InstantiationException, IOException, DecoderException, NoSuchMethodException, InvocationTargetException {

        // given: some input to read
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // and: a Reader reading that input
        Reader reader = Reader.getFactory().createArtifact(mock(Command.class));

        // when: input is read
        String output = reader.read(inputStream);

        // then: the input should actually get read
        assertEquals(input, output);
    }
}
