package org.rustygnome.tur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.rustygnome.tur.factory.Factored;
import org.rustygnome.tur.factory.Factory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandTest {

    static public Command aCommand(String commandLine) {
        String[] args = commandLine.split(" ");
        Command command = Command.getInstance().setupOptions().processArgs(args);
        Factored.setInstance(Command.class, command);
        return command;
    }

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    @BeforeEach
    public void clearFactored() {
        Factored.clearInstances();
    }

    @Test
    public void creatingAnInstance_shouldReturnAnInstance() {

        // when: getting an instance
        Command instance = Command.getInstance();

        // then: it should be an instance
        assertEquals(Command.class, instance.getClass());
    }

    @Test
    public void getInstance_shouldUseTheFactory() {

        // given: a mocked Command
        Command mockedCommand = mock(Command.class);
        // and: a mocked Factory
        Factory<Command> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact()).thenReturn(mockedCommand);
        Factory.setInstance(Command.class, mockedFactory);

        // when: getInstance() is called
        Command command = Command.getInstance();

        // then: the factories createArtefact method should get called
        verify(mockedFactory, times(1)).createArtifact();

        // and: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedCommand, command);
    }

    @ParameterizedTest
    @EnumSource(ImplementedOption.class)
    public void anyCliArgumentValueSpecifiedPerString_shouldResultInOptionValues(ImplementedOption option) {

        // given: a command object
        Command command = aCommand("");

        // when: CLI args without input option are processed
        String cliString = String.format("--%s %s", option.optionString, "someArbitraryValue");
        String[] args = cliString.split(" ", 2);
        command.processArgs(args);

        // then: no input should have been set
        if (option.hasValue) {
            assertEquals("someArbitraryValue", Command.getOptionValue(option.optionString));
        } else {
            assertTrue(Command.hasOption(option.optionString));
        }
    }

    @ParameterizedTest
    @EnumSource(ImplementedOption.class)
    public void anyCliArgumentValueSpecifiedPerChar_shouldResultInOptionValues(ImplementedOption option) {

        // given: a command object
        Command command = aCommand("");

        // when: CLI args without input option are processed
        final String cliString = String.format("--%s %s", option.optionChar, "someArbitraryValue");
        String[] args = cliString.split(" ", 2);
        command.processArgs(args);

        // then: no input should have been set
        if (option.hasValue) {
            assertEquals("someArbitraryValue", Command.getOptionValue(option.optionString));
        } else {
            assertTrue(Command.hasOption(option.optionString));
        }
    }

    @ParameterizedTest
    @EnumSource(ImplementedOption.class)
    public void missingCliArguments_shouldResultInMissingOptionValues(ImplementedOption option) {

        // given: a command object
        Command command = aCommand("");

        // when: those args are processed
        String[] cliArgs = new String[]{"hallo ---s du da"};
        command.processArgs(cliArgs);

        // then: no input should have been set
        assertNull(Command.getOptionValue(option.optionString));
    }

    @Test
    public void anUndefinedCliArgumentValue_shouldResultInTheUsageMessage()
            throws UnsupportedEncodingException {

        // given: a command object
        Command command = Command.getInstance().setupOptions();
        // and: a captured StdErr output
        ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stdOut, true, "UTF-8"));

        // when: CLI args with an undefined option "-r"
        String[] args = new String[]{"-r"};
        command.processArgs(args);

        // then: no input should have been set
        assertTrue(stdOut.toString().trim().contains("usage:"));
    }

    enum ImplementedOption {
        ACTION('a', "action", false),
        ECHO('e', "echo", false),
        INPUT('i', "input", true),
        OUTPUT('o', "output", true),
        SHEET('s', "sheet", true),
        TIME('t', "time", false),
        VERSION('v', "version", false);

        public char optionChar;
        public String optionString;
        public boolean hasValue;

        ImplementedOption(char optionChar, String optionString, boolean hasValue) {
            this.optionChar = optionChar;
            this.optionString = optionString;
            this.hasValue = hasValue;
        }
    }
}
