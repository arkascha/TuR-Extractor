package org.rustygnome.tur.artefact;

import org.apache.commons.codec.DecoderException;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factory;

import java.io.IOException;
import java.io.InputStreamReader;

public class Controller {

    private Logger logger;
    private Parser parser;
    private Reader reader;
    private Writer writer;

    static public Controller getInstance()
            throws IllegalAccessException, InstantiationException {
        return Factory.getInstance(Controller.class).createArtefact();
    }

    public Controller()
            throws IllegalAccessException, InstantiationException {
        logger = Logger.getInstance();
        parser = Parser.getInstance();
        reader = Reader.getInstance();
        writer = Writer.getInstance();
    }

    public void run()
            throws IOException, DecoderException {
        InputStreamReader input = new InputStreamReader(System.in);

        String message = reader.read(input);
        Values values = parser.parse(message);
        Boolean exported = writer.write(values);
        logger.log(exported, values);
    }
}
