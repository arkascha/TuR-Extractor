package org.rustygnome.tur.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class LoggerTest {

    static final private String FROZEN_TIME_2014 = "2014-12-22 14:12:22";
    static final private String FROZEN_TIME_2016 = "2016-09-02 16:09:02";

    private ByteArrayOutputStream stdOut;

    @BeforeEach
    private void setUpStdOut()
            throws UnsupportedEncodingException {
        stdOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stdOut, true, "UTF-8"));
    }

    private Clock aFrozenClock(String frozenTime) {
        return Clock.fixed(
                LocalDateTime
                    .parse(
                        frozenTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss" , Locale.getDefault()))
                    .toInstant(ZoneOffset.from(ZoneOffset.UTC)),
                ZoneId.of(ZoneOffset.UTC.getId()));
    }

    private Command aCliCommand(String[] args)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return Command
                .getInstance(null)
                .setupOptions()
                .processArgs(args);
    }

    @Test
    public void setClock_shouldSetTheClock()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        // given: a Logger
        Logger logger = Logger.getInstance(mock(Command.class));

        // when: some values are logged
        logger.setClock(aFrozenClock(FROZEN_TIME_2014));
        //then: the clock should have been frozen as expected
        assertEquals(FROZEN_TIME_2014, logger.currentDateTime());

        // when: some values are logged
        logger.setClock(aFrozenClock(FROZEN_TIME_2016));
        //then: the clock should have been frozen as expected
        assertEquals(FROZEN_TIME_2016, logger.currentDateTime());
    }

    @ParameterizedTest
    @MethodSource("actionMapping")
    public void cliArgumentAction_shouldOutputProcessedValues(boolean exported, String action)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        // given: a Command that specifies the "action" option
        Command command = aCliCommand(new String[]{"--action"});
        // and: some parsed Values
        Values values = Values.getTitles();
        // and: a Logger with frozen time
        Logger logger = Logger.getInstance(command).setClock(aFrozenClock(FROZEN_TIME_2014));

        // when: those Values are logged
        logger.log(exported, values);

        // then: the output should contain those Values
        assertEquals(action, stdOut.toString().trim());
    }

    static private Stream<Arguments> actionMapping() {
        return Stream.of(
                Arguments.of(true, "<EXPORTED>"),
                Arguments.of(false, "<IGNORED>")
        );
    }

    @Test
    public void cliArgumentEcho_shouldOutputProcessedValues()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        // given: a Command that specifies the "echo" option
        Command command = aCliCommand(new String[]{"--echo"});
        // and: some parsed Values
        Values values = Values.getTitles();
        // and: a Logger with frozen time
        Logger logger = Logger.getInstance(command).setClock(aFrozenClock(FROZEN_TIME_2014));

        // when: those Values are logged
        logger.log(true, values);

        // then: the output should contain those Values
        assertEquals(values.toString(), stdOut.toString().trim());
    }

    @Test
    public void cliArgumentTime_shouldOutputTimeOfAction()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        // given: a Command that specifies the "time" option
        Command command = aCliCommand(new String[]{"--time"});
        // and: a Logger with frozen time
        Logger logger = Logger.getInstance(command).setClock(aFrozenClock(FROZEN_TIME_2014));

        // when: some Values are logged
        logger.log(true, Values.getTitles());

        // then: the output should contain the expected time of action
        assertEquals('[' + FROZEN_TIME_2014 + ']', stdOut.toString().trim());
    }
}
