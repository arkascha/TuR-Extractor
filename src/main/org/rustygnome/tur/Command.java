package org.rustygnome.tur;

import org.apache.commons.cli.*;
import org.rustygnome.tur.factory.Factored;

public class Command
        extends Factored<Command> {

    static final String TAG = Command.class.getSimpleName();

    private Options options;
    private CommandLine commandLine;

    static public Command getInstance() {
        return (Command) Factored.getInstance(Command.class);
    }

    static public boolean hasOption(String optionName)  {
        CommandLine commandLine = getInstance().getCommandLine();
        return commandLine != null && commandLine.hasOption(optionName);
    }

    static public String getOptionValue(String optionName)  {
        CommandLine commandLine = getInstance().getCommandLine();
        if (commandLine != null) {
            return getInstance().getCommandLine().getOptionValue(optionName);
        }
        return null;
    }

    static public String getOptionValue(String optionName, String defaultValue)  {
        CommandLine commandLine = getInstance().getCommandLine();
        if (commandLine != null) {
            return getInstance().getCommandLine().getOptionValue(optionName, defaultValue);
        }
        return null;
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

        Option echoOption = new Option("e", "echo", false, "echo exported values");
        echoOption.setRequired(false);
        options.addOption(echoOption);

        Option infileOption = new Option("i", "infile", true, "input file path");
        infileOption.setRequired(false);
        options.addOption(infileOption);

        Option inpatternOption = new Option("I", "inpattern", true, "input file pattern");
        inpatternOption.setRequired(false);
        options.addOption(inpatternOption);

        Option logOption = new Option("l", "log", false, "log debug output");
        logOption.setRequired(false);
        options.addOption(logOption);

        Option outfileOption = new Option("o", "outfile", true, "output file path");
        outfileOption.setRequired(false);
        options.addOption(outfileOption);

        Option sheetOption = new Option("s", "sheet", true, "document sheet name");
        sheetOption.setRequired(false);
        options.addOption(sheetOption);

        Option timeOption = new Option("t", "time", false, "output time of action");
        timeOption.setRequired(false);
        options.addOption(timeOption);

        Option versionOption = new Option("v", "version", false, "version number");
        versionOption.setRequired(false);
        options.addOption(versionOption);

        Option helpOption = new Option("h", "help", false, "print usage help");
        helpOption.setRequired(false);
        options.addOption(helpOption);

        Option usageOption = new Option("?", "usage", false, "print usage help");
        usageOption.setRequired(false);
        options.addOption(usageOption);

        return this;
    }

    public Command processArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            commandLine = parser.parse(options, args);
        } catch (Exception e) {
            formatter.printHelp(Application.packageTitle, options);
            return null;
        }
        return this;
    }

    public void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( Application.packageTitle, options );
    }
}
