package org.rustygnome.tur.artifact;

import org.jetbrains.annotations.NotNull;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factory;

import java.time.LocalDate;
import java.time.LocalTime;

public class Logger
        extends Artifact {

    static public Factory<Logger> getFactory() {
        return Factory.getInstance(Logger.class);
    }

    public Logger(Command command) {
        super(command);
    }

    public void log(@NotNull String message) {
        System.out.println(String.format(
                "[%s %s] %s\n",
                LocalDate.now(),
                LocalTime.now(),
                message));
    }

    public void log(boolean exported, @NotNull Values values) {
        log(String.format(
                "%s: \n%s",
                exported ? "EXPORTED" : "IGNORED",
                values.toString()));
    }
}
