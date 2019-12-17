package org.rustygnome.tur.factory;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;
import org.rustygnome.tur.Command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class FactoredTest {

    @ParameterizedTest
    @MethodSource("allFactoredClasses")
    public void factoredClassesShouldBeAbleToCreateFactoringFactories(Class<? extends Factored> factoredClass)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        // given: a factoredClass and its getFactory method
        Method getFactory = factoredClass.getMethod("getFactory");
        Factory factory = (Factory) getFactory.invoke(null);

        // when: an artifact is created using that factory
        Object factoredObject = factory.createArtifact(mock(Command.class));

        // then: the factoredObject should be of type factoredClass
        assertEquals(factoredClass, factoredObject.getClass());
    }

    static private Set<Class<? extends Factored>> allFactoredClasses() {
        Reflections reflections = new Reflections("org.rustygnome.tur");
        return reflections.getSubTypesOf(Factored.class);
    }
}
