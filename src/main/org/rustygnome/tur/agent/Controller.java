package org.rustygnome.tur.agent;

import org.rustygnome.tur.Application;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factored;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Controller
        extends Factored<Controller> {

    static final String TAG = Controller.class.getSimpleName();

    static public Controller getInstance() {
        return (Controller) Factored.getInstance(Controller.class);
    }

    public Controller() {
        super();

        if (Command.hasOption("version")) {
            outputPackageInformation();
        }
    }

    public void run() {
        Logger.getInstance().logDebug(TAG, "controller running");

        String message = Reader.getInstance().read(setupInput());
        Values values = Parser.getInstance().parse(message);
        boolean exported = Writer.getInstance().write(values);
        Logger.getInstance().logValues(exported, values);

        Logger.getInstance().logDebug(TAG, "controller finished");
    }

    private InputStream setupInput() {
        Logger.getInstance().logDebug(TAG, "setting up input");

        if (Command.hasOption("input")) {

            String inputOptionValue = Command.getOptionValue("input", null);
            if (inputOptionValue.equals("-")) {
                Logger.getInstance().logDebug(TAG, "reading from StdIn");
                return System.in;
            }
            Logger.getInstance().logDebug(TAG, "reading from file");
            try {
                return new FileInputStream(inputOptionValue);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Logger.getInstance().logDebug(TAG, "not reading any input");
        return null;
    }

    void outputPackageInformation() {
        Logger.getInstance().logResult(String.format(
                "version: %s (%s)",
                Application.packageTitle,
                Application.packageVersion));
    }
}
