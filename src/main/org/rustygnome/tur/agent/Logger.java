package org.rustygnome.tur.agent;

import org.jetbrains.annotations.NotNull;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factored;
import org.rustygnome.tur.factory.Factory;

import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class Logger
        extends Factored {

    private Clock clock;

    static public Factory<Logger> getFactory() {
        return Factory.getInstance(Logger.class);
    }

    static public Logger getInstance(Command command)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return getFactory().createArtifact(command);
    }

    public Logger(Command command) {
        super(command);
        this.clock = Clock.system(ZoneId.systemDefault());
    }

    public Logger setClock(Clock clock) {
        this.clock = clock;
        return this;
    }

    public String currentDateTime() {
        return LocalDate.now(clock) + " " + LocalTime.now(clock);
    }

    public void log(@NotNull String message) {
        StringBuilder stringBuilder = new StringBuilder();
        if (command.hasOption("time")) {
            stringBuilder.append("[").append(currentDateTime()).append("] ");
        }
        stringBuilder.append(message).append(" \n");
        System.out.println(stringBuilder.toString().trim());
    }

    public void log(boolean exported, Values values) {
        StringBuilder stringBuilder = new StringBuilder();
        if (command.hasOption("action")) {
            stringBuilder.append(exported ? "<EXPORTED>" : "<IGNORED>").append(" \n");
        } else {
            stringBuilder.append(" \n");
        }
        if (command.hasOption("echo")) {
            stringBuilder.append(values != null ? values.toString() : "-/-").append(" \n");
        }
        log(stringBuilder.toString());
    }
}
