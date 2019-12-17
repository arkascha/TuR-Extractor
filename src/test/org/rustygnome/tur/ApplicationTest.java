package org.rustygnome.tur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rustygnome.tur.factory.Factory;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ApplicationTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @Test
    public void creatingAnInstance_shouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // when: getting an instance
        Application instance = Application.getInstance();

        // then: it should be an instance
        assertEquals(Application.class, instance.getClass());
    }

    @Test
    public void getInstance_shouldUseTheFactory()
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Application mockedApplication = mock(Application.class);
        Factory<Application> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact(eq(null))).thenReturn(mockedApplication);
        Factory.setInstance(Application.class, mockedFactory);

        // when: getInstance() is called
        Application application = Application.getInstance();

        // then: the factories createArtefact method should get called
        verify(mockedFactory, times(1)).createArtifact(null);

        // and: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedApplication, application);
    }

    @Test
    public void readPackageInformation_shouldExtractInformationFromThePackageManifest()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        // given: an Application
        Application application = Application.getInstance();
        // and: the package information
        Package mockedPackage = mock(Package.class);
        when(mockedPackage.getImplementationTitle()).thenReturn("the title");
        when(mockedPackage.getImplementationVersion()).thenReturn("the version");

        // when: the package information is read
        application.readPackageInformation(mockedPackage);

        // then: the package information should be available
        assertEquals("the title", Application.packageTitle);
        assertEquals("the version", Application.packageVersion);
    }
}
