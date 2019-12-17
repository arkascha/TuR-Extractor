package org.rustygnome.tur.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.agent.*;

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
    public void createArtifact_shouldUseInstancesPreviouslySet(FactoredType factoredType)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Object mockedArtifact = mock(factoredType.type);
        Factory mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact(any(Command.class))).thenReturn(mockedArtifact);
        Factory.setInstance(factoredType.type, mockedFactory);

        // when: createArtifact() is called
        Method getFactory = factoredType.type.getMethod("getFactory");
        Factory factory = (Factory) getFactory.invoke(null);
        Object artifact = factory.createArtifact(mock(Command.class));

        // then: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedArtifact, artifact);
    }

    @ParameterizedTest
    @EnumSource(FactoredType.class)
    public void clearInstances_shouldClearInstances(FactoredType factoredType)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Object mockedArtifact = mock(factoredType.type);
        Factory mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact(any(Command.class))).thenReturn(mockedArtifact);
        Factory.setInstance(factoredType.type, mockedFactory);

        // when: createArtifact() is called
        Method getFactory = factoredType.type.getMethod("getFactory");
        Factory factory = (Factory) getFactory.invoke(null);
        Object artifact = factory.createArtifact(mock(Command.class));
        // then: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedArtifact, artifact);

        // when: factory is cleared
        Factory.clearInstances();
        // and: createArtifact() is called again
        factory = (Factory) getFactory.invoke(null);
        artifact = factory.createArtifact(mock(Command.class));
        // then: the returned artifact should be the one created by the mocked factory
        assertNotEquals(mockedArtifact, artifact);
        assertEquals(factoredType.type, artifact.getClass());
    }

    @ParameterizedTest
    @EnumSource(FactoredType.class)
    public void createArtifact_shouldCreateArtifactsOfDesiredType(FactoredType factoredType)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a factory
        Factory factory = Factory.getInstance(factoredType.type);

        // when: createArtifact() is called
        Object artifact = factory.createArtifact(mock(Command.class));

        // then: the returned artifact should have the desired type
        assertEquals(factoredType.type, artifact.getClass());
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
