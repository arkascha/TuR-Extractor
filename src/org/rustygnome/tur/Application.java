package org.rustygnome.tur;

import org.apache.commons.codec.DecoderException;
import org.rustygnome.tur.artefact.Controller;
import org.rustygnome.tur.artefact.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Application {

    static public void main(String[] args)
            throws Exception {

        Command command = new Command()
                .setupOptions()
                .processArgs(args);

        try {
            Controller controller = Controller.getFactory().createArtefact(command);
            controller.run();
        } catch(Exception e) {
            Logger.getFactory().createArtefact(command).log("FAILURE");
            throw e;
        }
    }

}
