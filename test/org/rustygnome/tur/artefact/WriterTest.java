package org.rustygnome.tur.artefact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.factory.Factory;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WriterTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @Test
    public void creatingAnInstance_ShouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // when: getting an instance
        Writer instance = Writer.getFactory().createArtefact(mock(Command.class));

        // then: it should be an instance
        assertEquals(Writer.class, instance.getClass());
    }

    @Test
    public void getInstance_ShouldUseTheFactory()
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Writer mockedWriter = mock(Writer.class);
        Factory<Writer> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtefact(any(Command.class))).thenReturn(mockedWriter);
        Factory.setInstance(Writer.class, mockedFactory);

        // when: getInstance() is called
        Writer writer = Writer.getFactory().createArtefact(mock(Command.class));

        // then: the returned artefact should be the one created by the mocked factory
        assertEquals(mockedWriter, writer);
    }

}
