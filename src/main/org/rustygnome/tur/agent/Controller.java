package org.rustygnome.tur.agent;

import org.rustygnome.tur.Application;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factored;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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

        if (Command.hasOption("infile")) {
            processInfileOption();
        }
        if (Command.hasOption("inpattern")) {
            processInpatternOption();
        }

        Logger.getInstance().logDebug(TAG, "controller finished");
    }

    private void processInput(InputStream input) {
        Logger.getInstance().logDebug(TAG, "processing input");

        String message = Reader.getInstance().read(input);
        Values values = Parser.getInstance().parse(message);
        boolean exported = Writer.getInstance().write(values);
        Logger.getInstance().logValues(exported, values);
    }

    private void processInfileOption() {
        String inputFilePath = Command.getOptionValue("infile", null);
        if (inputFilePath != null) {
            if (inputFilePath.equals("-")) {
                Logger.getInstance().logDebug(TAG, "reading from StdIn");
                processInput(System.in);
            } else {
                Logger.getInstance().logDebug(TAG, String.format("reading from input file '%s'", inputFilePath));
                processInput(setupInfile(inputFilePath));
                removeInputFile(inputFilePath);
            }
        }
    }

    private void processInpatternOption() {
        String inputOptionValue = Command.getOptionValue("inpattern", null);
        Map<String, InputStream> inputMap = setupInpattern(inputOptionValue);
        for (String inputKey : inputMap.keySet()) {
            Logger.getInstance().logDebug(TAG, String.format("reading from input file '%s'", inputKey));
            processInput(inputMap.get(inputKey));
            removeInputFile(inputKey);
        }
    }

    private void removeInputFile(String filePath) {
        if (Command.hasOption("remove")) {
            Logger.getInstance().logDebug(TAG, String.format("removing input file '%s'", filePath));
            File inputFile = new File(filePath);
            inputFile.deleteOnExit();
        }
    }

    private InputStream setupInfile(String inputOptionValue) {
        try {
            return new FileInputStream(inputOptionValue);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, InputStream> setupInpattern(String inputOptionValue) {
        Map<String, InputStream> inputFiles = new HashMap<>();

        try {
            File glob = new File(inputOptionValue);
            Files.newDirectoryStream(Paths.get(glob.getParent()), glob.getName()).forEach(
                    file -> {
                        try {
                            inputFiles.put(file.toString(), new FileInputStream(file.toString()));
                        } catch (FileNotFoundException e) {
                            Logger.getInstance().logDebug(TAG, String.format("ignoring input file '%s'", file));
                        }
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return inputFiles;
    }

    void outputPackageInformation() {
        Logger.getInstance().logResult(String.format(
                "version: %s (%s)",
                Application.packageTitle,
                Application.packageVersion));
    }
}
