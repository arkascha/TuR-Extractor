package org.rustygnome.tur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.rustygnome.tur.factory.Factory;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CommandTest {

    @BeforeEach
    public void clearFactory() {
        Factory.clearInstances();
    }

    private Command aCommand()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return Command.getFactory().createArtifact(null).setupOptions();
    }

    @Test
    public void creatingAnInstance_shouldReturnAnInstance()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // when: getting an instance
        Command instance = Command.getFactory().createArtifact(null);

        // then: it should be an instance
        assertEquals(Command.class, instance.getClass());
    }

    @Test
    public void getInstance_shouldUseTheFactory()
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // given: a mocked factory
        Command mockedCommand = mock(Command.class);
        Factory<Command> mockedFactory = mock(Factory.class);
        when(mockedFactory.createArtifact(eq(null))).thenReturn(mockedCommand);
        Factory.setInstance(Command.class, mockedFactory);

        // when: getInstance() is called
        Command command = Command.getInstance(null);

        // then: the factories createArtefact method should get called
        verify(mockedFactory, times(1)).createArtifact(eq(null));

        // and: the returned artifact should be the one created by the mocked factory
        assertEquals(mockedCommand, command);
    }

    @ParameterizedTest
    @EnumSource(ImplementedOption.class)
    public void anyCliArgumentValueSpecifiedPerString_shouldResultInOptionValues(ImplementedOption option)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        // given: a command object
        Command command = aCommand();

        // when: CLI args without input option are processed
        String cliString = String.format("--%s %s", option.optionString, "someArbitraryValue");
        String[] args = cliString.split(" ", 2);
        command.processArgs(args);

        // then: no input should have been set
        if (option.hasValue) {
            assertEquals("someArbitraryValue", command.getOptionValue(option.optionString));
        } else {
            assertTrue(command.hasOption(option.optionString));
        }
    }

    @ParameterizedTest
    @EnumSource(ImplementedOption.class)
    public void anyCliArgumentValueSpecifiedPerChar_shouldResultInOptionValues(ImplementedOption option)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        // given: a command object
        Command command = aCommand();

        // when: CLI args without input option are processed
        final String cliString = String.format("--%s %s", option.optionChar, "someArbitraryValue");
        String[] args = cliString.split(" ", 2);
        command.processArgs(args);

        // then: no input should have been set
        if (option.hasValue) {
            assertEquals("someArbitraryValue", command.getOptionValue(option.optionString));
        } else {
            assertTrue(command.hasOption(option.optionString));
        }
    }

    @ParameterizedTest
    @EnumSource(ImplementedOption.class)
    public void missingCliArguments_shouldResultInMissingOptionValues(ImplementedOption option)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        // given: a command object
        Command command = aCommand();

        // when: those args are processed
        String[] cliArgs = new String[]{"hallo ---s du da"};
        command.processArgs(cliArgs);

        // then: no input should have been set
        assertNull(command.getOptionValue(option.optionString));
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
