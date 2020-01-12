package org.rustygnome.tur.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.rustygnome.tur.CommandTest;
import org.rustygnome.tur.factory.Factored;
import org.rustygnome.tur.factory.Factory;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReaderTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @BeforeEach
    public void clearFactored() {
        Factored.clearInstances();
    }

    @Test
    public void creatingAnInstance_shouldReturnAnInstance() {

        // when: getting an instance
        Reader instance = Reader.getInstance();

        // then: it should be an instance
        assertEquals(Reader.class, instance.getClass());
    }

    @Test
    public void getInstance_shouldUseTheFactory() {

        // given: a mocked factory
        Reader mockedReader = mock(Reader.class);
        Factory<Reader> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact()).thenReturn(mockedReader);
        Factory.setInstance(Reader.class, mockedFactory);

        // when: getInstance() is called
        Reader reader = Reader.getInstance();

        // then: the factories createArtefact method should get called
        verify(mockedFactory, times(1)).createArtifact();

        // and: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedReader, reader);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Das Pferd frisst keinen Kartoffelsalat.", "one \ntwo \nthree", ""})
    public void read_shouldReadTheInput(String input) {

        // given: a Command that specifies the "version" option
        CommandTest.aCommand("-i -");
        // and: some input to read
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        // and: some output buffer
        StringBuffer outputBuffer = new StringBuffer();

        // and: a Reader reading that input
        Reader reader = Factored.getFactory(Reader.class).createArtifact();

        // when: input is read
        reader.readInput(inputStream, outputBuffer);

        // then: the input should actually get read
        assertEquals(input, outputBuffer.toString());
    }
}
