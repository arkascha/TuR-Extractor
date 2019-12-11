package org.rustygnome.tur.artifact;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.codec.DecoderException;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

public class Controller
        extends Artifact {

    private Logger logger;
    private Parser parser;
    private Reader reader;
    private Writer writer;

    static public Factory<Controller> getFactory() {
        return Factory.getInstance(Controller.class);
    }

    public Controller(Command command)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        super(command);

        logger = Logger.getFactory().createArtifact(command);
        parser = Parser.getFactory().createArtifact(command);
        reader = Reader.getFactory().createArtifact(command);
        writer = Writer.getFactory().createArtifact(command);
    }

    public void run()
            throws IOException, DecoderException, MissingArgumentException {

        InputStreamReader input = setupInput(command.getOption("input", null));

        String message = reader.read(input);
        Values values = parser.parse(message);
        boolean exported = writer.write(values);
        logger.log(exported, values);
    }

    private InputStreamReader setupInput(String inputOption)
            throws FileNotFoundException {
        if (inputOption == null) {
            return new InputStreamReader(System.in);
        } else {
            return new InputStreamReader(new FileInputStream(inputOption));
        }
    }
}