package org.rustygnome.tur.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.artefact.*;
import org.rustygnome.tur.factory.Factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FactoryTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @ParameterizedTest
    @EnumSource(FactoredType.class)
    public void createArtefact_ShouldUseInstancesPreviouslySet(FactoredType factoredType)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Object mockedArtefact = mock(factoredType.type);
        Factory mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtefact(any(Command.class))).thenReturn(mockedArtefact);
        Factory.setInstance(factoredType.type, mockedFactory);

        // when: createArtefact() is called
        Method getFactory = factoredType.type.getMethod("getFactory");
        Factory factory = (Factory) getFactory.invoke(null);
        Object artefact = factory.createArtefact(mock(Command.class));

        // then: the returned artefact should be the one created by the mocked factory
        assertEquals(mockedArtefact, artefact);
    }

    @ParameterizedTest
    @EnumSource(FactoredType.class)
    public void clearInstances_ShouldClearInstances(FactoredType factoredType)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Object mockedArtefact = mock(factoredType.type);
        Factory mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtefact(any(Command.class))).thenReturn(mockedArtefact);
        Factory.setInstance(factoredType.type, mockedFactory);

        // when: createArtefact() is called
        Method getFactory = factoredType.type.getMethod("getFactory");
        Factory factory = (Factory) getFactory.invoke(null);
        Object artefact = factory.createArtefact(mock(Command.class));
        // then: the returned artefact should be the one created by the mocked factory
        assertEquals(mockedArtefact, artefact);

        // when: factory is cleared
        Factory.clearInstances();
        // and: createArtefact() is called again
        factory = (Factory) getFactory.invoke(null);
        artefact = factory.createArtefact(mock(Command.class));
        // then: the returned artefact should be the one created by the mocked factory
        assertNotEquals(mockedArtefact, artefact);
        assertEquals(factoredType.type, artefact.getClass());
    }

    @ParameterizedTest
    @EnumSource(FactoredType.class)
    public void createArtefact_ShouldCreateArtefactsOfDesiredType(FactoredType factoredType)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a factory
        Factory factory = Factory.getInstance(factoredType.type);

        // when: createArtefact() is called
        Object artefact = factory.createArtefact(mock(Command.class));

        // then: the returned artefact should have the desired type
        assertEquals(factoredType.type, artefact.getClass());
    }

    enum FactoredType {
        CONTROLLER(Controller.class),
        LOGGER(Logger.class),
        PARSER(Parser.class),
        READER(Reader.class),
        WRITER(Writer.class);

        public Class type;

        FactoredType(Class type) {
            this.type = type;
        }
    }
}
