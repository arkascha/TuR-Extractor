package org.rustygnome.tur.agent;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.codec.DecoderException;
import org.rustygnome.tur.Application;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factored;
import org.rustygnome.tur.factory.Factory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class Controller
        extends Factored {

    private Logger logger;
    private Parser parser;
    private Reader reader;
    private Writer writer;

    static public Factory<Controller> getFactory() {
        return Factory.getInstance(Controller.class);
    }

    static public Controller getInstance(Command command)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return getFactory().createArtifact(command);
    }

    public Controller(Command command)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        super(command);

        logger = Logger.getFactory().createArtifact(command);
        parser = Parser.getFactory().createArtifact(command);
        reader = Reader.getFactory().createArtifact(command);
        writer = Writer.getFactory().createArtifact(command);

        if (command.hasOption("version")) {
            outputPackageInformation();
        }
    }

    public void run()
            throws IOException, DecoderException, MissingArgumentException {

        String message = reader.read(setupInput());
        Values values = parser.parse(message);
        boolean exported = writer.write(values);
        logger.log(exported, values);
    }

    private InputStream setupInput()
            throws FileNotFoundException {

        if (command.hasOption("input")) {

            String inputOptionValue = command.getOptionValue("input", null);
            if (inputOptionValue.equals("-")) {
                return System.in;
            }
            return new FileInputStream(inputOptionValue);
        }

        return null;
    }

    void outputPackageInformation() {
        System.err.println(String.format(
                "%s (version %s)",
                Application.packageName,
                Application.packageVersion));
    }
}
