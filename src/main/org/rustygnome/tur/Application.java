package org.rustygnome.tur;

import org.rustygnome.tur.agent.Controller;
import org.rustygnome.tur.agent.Logger;

public class Application {

    static final public String APPLICATION_NAME = "TuR-Extractor";
    static final public String APPLICATION_VERSION = "0.0.6";

    static public void main(String[] args)
            throws Exception {

        Command command = Command
                .getInstance(null)
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
            if (command.hasOption("action")) {
                Logger.getFactory().createArtifact(command).log("FAILURE");
            }
            throw e;
        }
    }
}
