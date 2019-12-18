package org.rustygnome.tur.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;
import org.rustygnome.tur.CommandTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FactoredTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @BeforeEach
    public void clearFactored() {
        Factored.clearInstances();
    }

    @ParameterizedTest
    @MethodSource("allFactoredClasses")
    public void getInstance_shouldReturnAnExistingInstance(Class<? extends Factored> factoredClass)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // given: a Command
        CommandTest.aCommand("");
        // and: the getInstance method of the factoredClass
        Method getFactory = factoredClass.getMethod("getInstance");

        // when: a first instance
        Factored firstArtifact = (Factored) getFactory.invoke(null);
        // and: a second instance
        Factored secondArtifact = (Factored) getFactory.invoke(null);

        // then: the two factories should differ
        assertEquals(firstArtifact, secondArtifact);
    }

    @ParameterizedTest
    @MethodSource("allFactoredClasses")
    public void clearInstances_shouldClearInstances(Class<? extends Factored> factoredClass) {

        // given: a Command
        CommandTest.aCommand("");
        // and: a Factory
        Factory factory = Factory.getInstance(factoredClass);

        // when: a first Artifact is created
        Factored firstArtifact = (Factored) factory.createArtifact();
        // and: factored instances are cleared
        Factored.clearInstances();
        // and: the same Command is used
        CommandTest.aCommand("");
        // and: a second Artifact is created
        Factored secondArtifact = (Factored) factory.createArtifact();

        // then: the two factories should differ
        assertNotEquals(firstArtifact, secondArtifact);
    }

    static private Set<Class<? extends Factored>> allFactoredClasses() {
        Reflections reflections = new Reflections("org.rustygnome.tur");
        return reflections.getSubTypesOf(Factored.class);
    }
}
