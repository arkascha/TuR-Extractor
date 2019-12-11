package org.rustygnome.tur;

import org.rustygnome.tur.artifact.Controller;
import org.rustygnome.tur.artifact.Logger;

public class Application {

    static public void main(String[] args)
            throws Exception {

        Command command = new Command()
                .setupOptions()
                .processArgs(args);

        try {
            Controller controller = Controller.getFactory().createArtifact(command);
            controller.run();
        } catch(Exception e) {
            Logger.getFactory().createArtifact(command).log("FAILURE");
            throw e;
        }
    }
}
