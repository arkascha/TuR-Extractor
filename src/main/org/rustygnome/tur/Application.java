package org.rustygnome.tur;

import org.rustygnome.tur.artifact.Controller;
import org.rustygnome.tur.artifact.Logger;

public class Application {

    static final String APPLICATION_NAME = "TuR-Extractor";
    static final String APPLICATION_VERSION = "0.0.6";

    static public void main(String[] args)
            throws Exception {

        Command command = new Command()
                .setupOptions()
                .processArgs(args);

        if (command.hasOption("version")) {
            System.err.println(String.format(
                    "%s (version %s)",
                    APPLICATION_NAME,
                    APPLICATION_VERSION
            ));
        }

        try {
            Controller controller = Controller.getFactory().createArtifact(command);
            controller.run();
        } catch(Exception e) {
            Logger.getFactory().createArtifact(command).log("FAILURE");
            throw e;
        }
    }
}
