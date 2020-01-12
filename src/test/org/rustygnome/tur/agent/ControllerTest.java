package org.rustygnome.tur.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rustygnome.tur.Application;
import org.rustygnome.tur.CommandTest;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factored;
import org.rustygnome.tur.factory.Factory;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControllerTest {

    private ByteArrayOutputStream stdErr;

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

        // given: a Command
        CommandTest.aCommand("");

        // when: getting an instance
        Controller instance = Controller.getInstance();

        // then: it should be an instance
        assertEquals(Controller.class, instance.getClass());
    }

    @Test
    public void getInstance_shouldUseTheFactory() {

        // given: a mocked factory
        Controller mockedController = mock(Controller.class);
        Factory<Controller> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact()).thenReturn(mockedController);
        Factory.setInstance(Controller.class, mockedFactory);

        // when: getInstance() is called
        Controller controller = Controller.getInstance();

        // then: the factories createArtefact method should get called
        verify(mockedFactory, times(1)).createArtifact();

        // and: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedController, controller);
    }
}
