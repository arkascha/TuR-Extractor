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
        output.setRequired(true);
        options.addOption(output);

        Option sheet = new Option("s", "sheet", true, "document sheet name");
        sheet.setRequired(false);
        options.addOption(sheet);

        return this;
    }

    public Command processArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(COMMAND_NAME, options);

            System.exit(1);
        }

        return this;
    }

    public String getOption(String optionName) {
        return cmd.getOptionValue(optionName);
    }

    public String getOption(String optionName, String defaultValue) {
        return cmd.getOptionValue(optionName, defaultValue);
    }

    public String getOption(char optionChar) {
        return cmd.getOptionValue(optionChar);
    }

    public String getOption(char optionChar, String defaultValue) {
        return cmd.getOptionValue(optionChar, defaultValue);
    }
}
