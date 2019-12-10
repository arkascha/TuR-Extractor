package org.rustygnome.tur.artefact;

import com.sun.istack.internal.NotNull;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factory;

import java.time.LocalDate;
import java.time.LocalTime;

public class Logger {

    static public Logger getInstance() throws IllegalAccessException, InstantiationException {
        return Factory.getInstance(Logger.class).createArtefact();
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
