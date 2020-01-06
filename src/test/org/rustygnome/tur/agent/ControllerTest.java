package org.rustygnome.tur.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rustygnome.tur.Application;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.CommandTest;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factored;
import org.rustygnome.tur.factory.Factory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

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

    @Test
    public void run_shouldUseAllArtifactsByTheirMainPurpose() {

        // given: a command
        CommandTest.aCommand("-i -");
        // and: some input to read
        final String input = "Postprophylaktische Suboptimalität";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // and: some mocked artifacts
        Logger mockedLogger = mock(Logger.class);
        Factored.setInstance(Logger.class, mockedLogger);

        Parser mockedParser = mock(Parser.class);
        when(mockedParser.parse(anyString())).thenReturn(mock(Values.class));
        Factored.setInstance(Parser.class, mockedParser);

        Reader mockedReader = mock(Reader.class);
        when(mockedReader.read(any())).thenReturn("Blocksberg");
        Factored.setInstance(Reader.class, mockedReader);

        Writer mockedWriter = mock(Writer.class);
        when(mockedWriter.write(any())).thenReturn(true);
        Factored.setInstance(Writer.class, mockedWriter);

        // and: a Controller
        Controller controller = Factored.getFactory(Controller.class).createArtifact();

        // when: running that controller
        controller.run();

        // then: all artifacts should get used by their main purpose
        verify(mockedLogger, times(1)).logValues(anyBoolean(), any());
        verify(mockedParser, times(1)).parse(anyString());
        verify(mockedReader, times(1)).read(any());
        verify(mockedWriter, times(1)).write(any());
    }

    @Test
    public void cliArgumentVersion_shouldOutputProcessedValues()
            throws UnsupportedEncodingException {

        // given: a Command that specifies the "version" option
        CommandTest.aCommand("-v");
        // and: a captured StdErr output
        ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stdOut, true, "UTF-8"));

        // when: a Controller ist instantiated
        Controller.getInstance();

        // then: the output should contain the package information
        String expectedInformation = String.format(
                "version: %s (%s)",
                Application.packageTitle,
                Application.packageVersion);
        assertEquals(expectedInformation, stdOut.toString().trim());
    }
}
