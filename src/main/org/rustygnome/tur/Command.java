package org.rustygnome.tur;

import org.apache.commons.cli.*;

public class Command {

    static private final String COMMAND_NAME = "tur-extract";

    private Options options;
    private CommandLine cmd;

    public Command setupOptions() {
        options = new Options();

        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(false);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file path");
        output.setRequired(false);
        options.addOption(output);

        Option sheet = new Option("s", "sheet", true, "document sheet name");
        sheet.setRequired(false);
        options.addOption(sheet);

        Option version = new Option("v", "version", false, "version number");
        version.setRequired(false);
        options.addOption(version);

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
