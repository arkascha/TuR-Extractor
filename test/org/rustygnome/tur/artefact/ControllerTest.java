package org.rustygnome.tur.artefact;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rustygnome.tur.artefact.Controller;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @Test
    public void run_ShouldUseAllArtefactsByTheirMainPurpose()
            throws InstantiationException, IllegalAccessException, IOException, DecoderException {

        // given: some mocked artefacts
        Logger mockedLogger = mock(Logger.class);

        Parser mockedParser = mock(Parser.class);
        when(mockedParser.parse(anyString())).thenReturn(mock(Values.class));

        Reader mockedReader = mock(Reader.class);
        when(mockedReader.read(any())).thenReturn("Blocksberg");

        Writer mockedWriter = mock(Writer.class);
        when(mockedWriter.write(any())).thenReturn(true);

        // and: mocked factories returning those mocked artefacts
        Factory<Logger> mockedLoggerFactory = mock(Factory.class);
        when(mockedLoggerFactory.createArtefact()).thenReturn(mockedLogger);
        Factory.setInstance(Logger.class, mockedLoggerFactory);

        Factory<Parser> mockedParserFactory = mock(Factory.class);
        when(mockedParserFactory.createArtefact()).thenReturn(mockedParser);
        Factory.setInstance(Parser.class, mockedParserFactory);

        Factory<Reader> mockedReaderFactory = mock(Factory.class);
        when(mockedReaderFactory.createArtefact()).thenReturn(mockedReader);
        Factory.setInstance(Reader.class, mockedReaderFactory);

        Factory<Writer> mockedWriterFactory = mock(Factory.class);
        when(mockedWriterFactory.createArtefact()).thenReturn(mockedWriter);
        Factory.setInstance(Writer.class, mockedWriterFactory);

        // and: a Controller
        Controller controller = Controller.getInstance();

        // when: running that controller
        controller.run();

        // then: all artefacts should get used once by their main purpose
        verify(mockedLogger, times(1)).log(anyBoolean(), any());
        verify(mockedParser, times(1)).parse(anyString());
        verify(mockedReader, times(1)).read(any());
        verify(mockedWriter, times(1)).write(any());
    }
}