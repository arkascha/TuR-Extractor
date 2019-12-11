package org.rustygnome.tur.artifact;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControllerTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @Test
    public void creatingAnInstance_ShouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // when: getting an instance
        Controller instance = Controller.getFactory().createArtifact(mock(Command.class));

        // then: it should be an instance
        assertEquals(Controller.class, instance.getClass());
    }

    @Test
    public void getInstance_ShouldUseTheFactory()
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Controller mockedController = mock(Controller.class);
        Factory<Controller> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact(any(Command.class))).thenReturn(mockedController);
        Factory.setInstance(Controller.class, mockedFactory);

        // when: getInstance() is called
        Controller controller = Controller.getFactory().createArtifact(mock(Command.class));

        // then: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedController, controller);
    }

    @Test
    public void run_ShouldUseAllArtifactsByTheirMainPurpose()
            throws InstantiationException, IllegalAccessException, IOException, DecoderException, NoSuchMethodException, InvocationTargetException, MissingArgumentException {

        // given: some mocked artifacts
        Logger mockedLogger = mock(Logger.class);

        Parser mockedParser = mock(Parser.class);
        when(mockedParser.parse(anyString())).thenReturn(mock(Values.class));

        Reader mockedReader = mock(Reader.class);
        when(mockedReader.read(any())).thenReturn("Blocksberg");

        Writer mockedWriter = mock(Writer.class);
        when(mockedWriter.write(any())).thenReturn(true);

        // and: mocked factories returning those mocked artifacts
        Factory<Logger> mockedLoggerFactory = mock(Factory.class);
        when(mockedLoggerFactory.createArtifact(any(Command.class))).thenReturn(mockedLogger);
        Factory.setInstance(Logger.class, mockedLoggerFactory);

        Factory<Parser> mockedParserFactory = mock(Factory.class);
        when(mockedParserFactory.createArtifact(any(Command.class))).thenReturn(mockedParser);
        Factory.setInstance(Parser.class, mockedParserFactory);

        Factory<Reader> mockedReaderFactory = mock(Factory.class);
        when(mockedReaderFactory.createArtifact(any(Command.class))).thenReturn(mockedReader);
        Factory.setInstance(Reader.class, mockedReaderFactory);

        Factory<Writer> mockedWriterFactory = mock(Factory.class);
        when(mockedWriterFactory.createArtifact(any(Command.class))).thenReturn(mockedWriter);
        Factory.setInstance(Writer.class, mockedWriterFactory);

        // and: a Controller
        Controller controller = Controller.getFactory().createArtifact(mock(Command.class));

        // when: running that controller
        controller.run();

        // then: all artifacts should get used once by their main purpose
        verify(mockedLogger, times(1)).log(anyBoolean(), any());
        verify(mockedParser, times(1)).parse(anyString());
        verify(mockedReader, times(1)).read(any());
        verify(mockedWriter, times(1)).write(any());
    }
}
