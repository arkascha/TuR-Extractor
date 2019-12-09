package org.rustygnome.tur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WriterTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @Test
    public void getInstance_ShouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException {

        // when: getting an instance
        Writer instance = Writer.getInstance();

        // then: it should be an instance
        assertEquals(Writer.class, instance.getClass());
    }

    @Test
    public void getInstance_ShouldUseTheFactory()
            throws InstantiationException, IllegalAccessException {

        // given: a mocked factory
        Writer mockedWriter = mock(Writer.class);
        Factory<Writer> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtefact()).thenReturn(mockedWriter);
        Factory.setInstance(Writer.class, mockedFactory);

        // when: getInstance() is called
        Writer writer = Writer.getInstance();

        // then: the returned artefact should be the one created by the mocked factory
        assertEquals(mockedWriter, writer);
    }

}
