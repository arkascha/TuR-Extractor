package org.rustygnome.tur;

import org.apache.commons.cli.*;
import org.rustygnome.tur.factory.Factored;
import org.rustygnome.tur.factory.Factory;

import java.lang.reflect.InvocationTargetException;

public class Command
        extends Factored {

    static private final String COMMAND_NAME = "tur-extract";

    private Options options;
    private CommandLine cmd;

    static public Factory<Command> getFactory() {
        return Factory.getInstance(Command.class);
    }

    static public Command getInstance(Command command)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return getFactory().createArtifact(command);
    }

    public Command(Command command)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        super(command);
    }

    public Command setupOptions() {
        options = new Options();

        Option actionOption = new Option("a", "action", false, "output performed action");
        actionOption.setRequired(false);
        options.addOption(actionOption);

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
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp(COMMAND_NAME, options);

            System.exit(1);
        }

        return this;
    }

    public boolean hasOption(String optionName) {
        return cmd.hasOption(optionName);
    }

    public String getOptionValue(String optionName) {
        return cmd.getOptionValue(optionName);
    }

    public String getOptionValue(String optionName, String defaultValue) {
        return cmd.getOptionValue(optionName, defaultValue);
    }
}
