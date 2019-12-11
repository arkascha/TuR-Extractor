package org.rustygnome.tur.artifact;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.factory.Factory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReaderTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @Test
    public void creatingAnInstance_ShouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // when: getting an instance
        Reader instance = Reader.getFactory().createArtifact(mock(Command.class));

        // then: it should be an instance
        assertEquals(Reader.class, instance.getClass());
    }

    @Test
    public void getInstance_ShouldUseTheFactory()
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Reader mockedReader = mock(Reader.class);
        Factory<Reader> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact(any(Command.class))).thenReturn(mockedReader);
        Factory.setInstance(Reader.class, mockedFactory);

        // when: getInstance() is called
        Reader reader = Reader.getFactory().createArtifact(mock(Command.class));

        // then: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedReader, reader);
    }

    @Test
    public void readInput_ShouldReadTheInput()
            throws IllegalAccessException, InstantiationException, IOException, DecoderException, NoSuchMethodException, InvocationTargetException {

        // given: some input to read
        final String INPUT = "Das Pferd frisst keinen Kartoffelsalat.";
        ByteArrayInputStream bais = new ByteArrayInputStream(INPUT.getBytes());
        InputStreamReader input = new InputStreamReader(bais);

        // and: a Reader reading that input
        Reader reader = Reader.getFactory().createArtifact(mock(Command.class));

        // when: input is read
        String output = reader.read(input);

        // then: the input should actually get read
        assertEquals(INPUT, output);
    }
}
