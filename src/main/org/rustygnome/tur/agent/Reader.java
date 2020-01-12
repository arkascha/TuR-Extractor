package org.rustygnome.tur.agent;

import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factored;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Reader
        extends Factored<Reader> {

    static final String TAG = Reader.class.getSimpleName();

    StringBuffer inputBuffer = new StringBuffer();

    static public Reader getInstance() {
        return (Reader) Factored.getInstance(Reader.class);
    }

    public Reader() {
        super();
    }

    public void read() {
        if (Command.hasOption("infile")) {
            Reader.getInstance().processInfileOption();
        }
        if (Command.hasOption("inpattern")) {
            Reader.getInstance().processInpatternOption();
        }
    }

    public void readInput(InputStream inputStream, StringBuffer outputBuffer) {
        Logger.getInstance().logDebug(TAG, "reading input");
        if (inputStream != null) {
            CharBuffer inputBuffer = CharBuffer.allocate(1000);
            try {
                InputStreamReader inputReader = new InputStreamReader(inputStream);
                while (inputReader.ready()) {
                    //noinspection ResultOfMethodCallIgnored
                    inputReader.read(inputBuffer);
                    inputBuffer.flip();
                    outputBuffer.append(inputBuffer.toString());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Logger.getInstance().logDebug(TAG, "read input chunk");
        }
    }

    private void processInput(InputStream input) {
        Logger.getInstance().logDebug(TAG, "processing input");

        try {
            Reader.getInstance().readInput(input, inputBuffer);
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Values values = Parser.getInstance().parse(inputBuffer);
        boolean exported = Writer.getInstance().write(values);

        Logger.getInstance().logValues(exported, values);
    }

    public void processInfileOption() {
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

    public void processInpatternOption() {
        String inputOptionValue = Command.getOptionValue("inpattern", null);
        Logger.getInstance().logDebug(TAG, String.format("scanning input pattern '%s'", inputOptionValue));
        Map<String, InputStream> inputMap = setupInpattern(inputOptionValue);
        for (String inputKey : inputMap.keySet()) {
            Logger.getInstance().logDebug(TAG, String.format("reading from input file '%s'", inputKey));
            processInput(inputMap.get(inputKey));
            removeInputFile(inputKey);
        }
    }

    private void removeInputFile(String filePath) {
        if (Command.hasOption("remove")) {
            File inputFile = new File(filePath);
            if (inputFile.isFile()) {
                Logger.getInstance().logDebug(TAG, String.format("removing input file '%s'", filePath));
                inputFile.delete();
            } else {
                Logger.getInstance().logDebug(TAG, String.format("'%s' is not a plain file, cowardly refusing to remove it", filePath));
            }
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
}
