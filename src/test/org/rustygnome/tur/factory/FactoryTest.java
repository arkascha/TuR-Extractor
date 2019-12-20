package org.rustygnome.tur.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.rustygnome.tur.Application;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.CommandTest;
import org.rustygnome.tur.agent.*;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FactoryTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @BeforeEach
    public void clearFactored() {
        Factored.clearInstances();
    }

    @ParameterizedTest
    @EnumSource(FactoredType.class)
    public void createArtifact_shouldUseInstancesPreviouslySet(FactoredType factoredType)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a Command
        CommandTest.aCommand("");

        // when: a first Factory is created
        Factory firstFactory = Factory.getInstance(factoredType.type);
        // and: a second Factory is created
        Factory secondFactory = Factory.getInstance(factoredType.type);

        // then: the two factories should be equal
        assertEquals(firstFactory, secondFactory);
    }

    @ParameterizedTest
    @EnumSource(FactoredType.class)
    public void clearInstances_shouldClearInstances(FactoredType factoredType)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a Command
        CommandTest.aCommand("");

        // when: a first Factory is created
        Factory firstFactory = Factory.getInstance(factoredType.type);
        // and: factory is cleared
        Factory.clearInstances();
        // and: a second Factory is created
        Factory secondFactory = Factory.getInstance(factoredType.type);

        // then: the two factories should differ
        assertNotEquals(firstFactory, secondFactory);
    }

    @ParameterizedTest
    @EnumSource(FactoredType.class)
    public void createArtifact_shouldCreateArtifactsOfDesiredType(FactoredType factoredType) {

        // given: a Command
        CommandTest.aCommand("");
        // and: a Factory
        Factory factory = Factory.getInstance(factoredType.type);

        // when: createArtifact() is called
        Object artifact = factory.createArtifact();

        // then: the returned artifact should have the desired type
        assertEquals(factoredType.type, artifact.getClass());
    }

    enum FactoredType {
        APPLICATION(Application.class),
        COMMAND(Command.class),
        CONTROLLER(Controller.class),
        LOGGER(Logger.class),
        PARSER(Parser.class),
        READER(Reader.class),
        WRITER(Writer.class);

        public Class<? extends Factored> type;

        FactoredType(Class<? extends Factored> type) {
            this.type = type;
        }
    }
}
