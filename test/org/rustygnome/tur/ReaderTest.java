package org.rustygnome.tur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
