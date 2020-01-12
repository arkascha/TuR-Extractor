package org.rustygnome.tur.agent;

import org.rustygnome.tur.Command;
import org.rustygnome.tur.factory.Factored;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Controller
        extends Factored<Controller> {

    static final String TAG = Controller.class.getSimpleName();

    static public Controller getInstance() {
        return (Controller) Factored.getInstance(Controller.class);
    }

    public void control() {
        Logger.getInstance().logDebug(TAG, "controller running");

        if (Command.hasOption("daemon")) {
            int period = 60;
            try {
                String optionValue = Command.getOptionValue("daemon");
                if (optionValue != null) {
                    period = Integer.parseInt(optionValue);
                } else {
                    throw new Exception("no daemon period value specified at all");
                }
                Logger.getInstance().logDebug(TAG, String.format("using a daemon period value of %d seconds", period));
            } catch (Exception e) {
                Logger.getInstance().logDebug(TAG, "no valid daemon period specified, leaving the default of 60 secs");
            }
            Logger.getInstance().logDebug(TAG, "entering daemon mode");
            Executors
                    .newScheduledThreadPool(1)
                    .scheduleAtFixedRate(() -> {
                        Logger.getInstance().logDebug(TAG, "periodic action run ...");
                        Reader.getInstance().read();
                    }, 0, period, TimeUnit.SECONDS);
        } else {
            Logger.getInstance().logDebug(TAG, "single action shot ...");
            Reader.getInstance().read();
        }

        Logger.getInstance().logDebug(TAG, "controller finished");
    }
}
