package org.rustygnome.tur.artefact;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rustygnome.tur.artefact.Reader;
import org.rustygnome.tur.factory.Factory;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReaderTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @Test
    public void getInstance_ShouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException {

        // when: getting an instance
        Reader instance = Reader.getInstance();

        // then: it should be an instance
        assertEquals(Reader.class, instance.getClass());
    }

    @Test
    public void getInstance_ShouldUseTheFactory()
            throws InstantiationException, IllegalAccessException {

        // given: a mocked factory
        Reader mockedReader = mock(Reader.class);
        Factory<Reader> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtefact()).thenReturn(mockedReader);
        Factory.setInstance(Reader.class, mockedFactory);

        // when: getInstance() is called
        Reader reader = Reader.getInstance();

        // then: the returned artefact should be the one created by the mocked factory
        assertEquals(mockedReader, reader);
    }

    @Test
    public void readInput_ShouldReadTheInput()
            throws IllegalAccessException, InstantiationException, IOException, DecoderException {

        // given: some input to read
        final String INPUT = "Das Pferd frisst keinen Kartoffelsalat.";
        ByteArrayInputStream bais = new ByteArrayInputStream(INPUT.getBytes());
        InputStreamReader input = new InputStreamReader(bais);

        // and: a Reader reading that input
        Reader reader = Reader.getInstance();

        // when: input is read
        String output = reader.read(input);

        // then: the input should actually get read
        assertEquals(INPUT, output);
    }
}
