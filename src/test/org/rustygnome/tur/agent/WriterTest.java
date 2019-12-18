package org.rustygnome.tur.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rustygnome.tur.factory.Factored;
import org.rustygnome.tur.factory.Factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WriterTest {

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
        Writer instance = Writer.getInstance();

        // then: it should be an instance
        assertEquals(Writer.class, instance.getClass());
    }

    @Test
    public void getInstance_shouldUseTheFactory() {

        // given: a mocked Writer factory
        Writer mockedWriter = mock(Writer.class);
        Factory<Writer> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact()).thenReturn(mockedWriter);
        Factory.setInstance(Writer.class, mockedFactory);

        // when: getInstance() is called
        Writer writer = Writer.getInstance();

        // then: the factories createArtefact method should get called
        verify(mockedFactory, times(1)).createArtifact();

        // and: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedWriter, writer);
    }

}
