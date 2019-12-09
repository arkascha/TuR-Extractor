package org.rustygnome.tur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControllerTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @Test
    public void getInstance_ShouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException {

        // when: getting an instance
        Controller instance = Controller.getInstance();

        // then: it should be an instance
        assertEquals(Controller.class, instance.getClass());
    }

    @Test
    public void getInstance_ShouldUseTheFactory()
            throws InstantiationException, IllegalAccessException {

        // given: a mocked factory
        Controller mockedController = mock(Controller.class);
        Factory<Controller> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtefact()).thenReturn(mockedController);
        Factory.setInstance(Controller.class, mockedFactory);

        // when: getInstance() is called
        Controller controller = Controller.getInstance();

        // then: the returned artefact should be the one created by the mocked factory
        assertEquals(mockedController, controller);
    }

}
