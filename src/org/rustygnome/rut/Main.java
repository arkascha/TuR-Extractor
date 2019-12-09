package org.rustygnome.rut;

import org.apache.commons.codec.DecoderException;

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
            System.out.println(
                    String.format(
                            "[%s %s] FAILURE\n\n",
                            LocalDate.now(),
                            LocalTime.now()
                    ));
            throw e;
        }
    }
}
