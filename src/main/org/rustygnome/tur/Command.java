package org.rustygnome.tur;

import org.apache.commons.cli.*;
import org.rustygnome.tur.agent.Logger;
import org.rustygnome.tur.factory.Factored;

public class Command
        extends Factored {

    static final String TAG = Command.class.getSimpleName();

    static private final String COMMAND_NAME = "tur-extract";

    private Options options;
    private CommandLine commandLine;

    static public Command getInstance() {
        return (Command) Factored.getInstance(Command.class);
    }

    static public boolean hasOption(String optionName)  {
        return getInstance().getCommandLine().hasOption(optionName);
    }

    static public String getOptionValue(String optionName)  {
        return getInstance().getCommandLine().getOptionValue(optionName);
    }

    static public String getOptionValue(String optionName, String defaultValue)  {
        return getInstance().getCommandLine().getOptionValue(optionName, defaultValue);
    }

    public Command() {
        super();
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

    public Command setupOptions() {
        options = new Options();

        Option actionOption = new Option("a", "action", false, "output performed action");
        actionOption.setRequired(false);
        options.addOption(actionOption);

        Option debugOption = new Option("d", "debug", false, "write debug output");
        debugOption.setRequired(false);
        options.addOption(debugOption);

        Option echoOption = new Option("e", "echo", false, "echo exported values");
        echoOption.setRequired(false);
        options.addOption(echoOption);

        Option inputOption = new Option("i", "input", true, "input file path");
        inputOption.setRequired(false);
        options.addOption(inputOption);

        Option outputOption = new Option("o", "output", true, "output file path");
        outputOption.setRequired(false);
        options.addOption(outputOption);

        Option sheetOption = new Option("s", "sheet", true, "document sheet name");
        sheetOption.setRequired(false);
        options.addOption(sheetOption);

        Option timeOption = new Option("t", "time", false, "output time of action");
        timeOption.setRequired(false);
        options.addOption(timeOption);

        Option versionOption = new Option("v", "version", false, "version number");
        versionOption.setRequired(false);
        options.addOption(versionOption);

        return this;
    }

    public Command processArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            Logger.getInstance().logException(TAG, e);
            formatter.printHelp(COMMAND_NAME, options);

            System.exit(1);
        }

        return this;
    }
}
