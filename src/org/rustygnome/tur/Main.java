package org.rustygnome.tur;

import org.apache.commons.codec.DecoderException;
import org.rustygnome.tur.artefact.Controller;
import org.rustygnome.tur.artefact.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args)
            throws IOException, IllegalAccessException, InstantiationException, DecoderException {
        try {
            Controller controller = Controller.getInstance();
            controller.run();
        } catch(Exception e) {
            Logger.getInstance().log("FAILURE");
            throw e;
        }
    }
}
