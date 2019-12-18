package org.rustygnome.tur.agent;

import org.jetbrains.annotations.NotNull;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factored;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class Logger
        extends Factored {

    static final String TAG = Logger.class.getSimpleName();

    private Clock clock;

    static public Logger getInstance() {
        return (Logger) Factored.getInstance(Logger.class);
    }

    public Logger() {
        super();
        this.clock = Clock.system(ZoneId.systemDefault());
    }

    public Logger setClock(Clock clock) {
        this.clock = clock;
        return this;
    }

    public String currentDateTime() {
        return LocalDate.now(clock) + " " + LocalTime.now(clock);
    }

    public void log(@NotNull String message, boolean debug) {
        if (debug && Command.hasOption("debug")) {
            System.err.println(message);
            System.err.flush();
        } else {
            System.out.println(message);
            System.out.flush();
        }
    }

    public void logDebug(String tag, String message) {
        if (Command.hasOption("debug")) {
            getInstance().log(tag + ": " + message, true);
        }
    }

    public void logException(String tag, Exception e) {
        Logger logger = getInstance();
        logger.log(tag + ": exception caught: ", true);
        logger.log(e.getMessage(), true);
    }

    public void logResult(String message) {
        getInstance().logDebug(TAG, "logging result");
        getInstance().log(message, false);
    }

    public void logValues(boolean exported, Values values) {
        getInstance().logDebug(TAG, "logging values");

        StringBuilder stringBuilder = new StringBuilder();
        if (Command.hasOption("time")) {
            stringBuilder.append("time: ").append(getInstance().currentDateTime()).append("\n");
        }
        if (Command.hasOption("action")) {
            stringBuilder.append("action: ").append(exported ? "EXPORTED" : "IGNORED").append("\n");
        }
        if (Command.hasOption("echo")) {
            stringBuilder.append("result:\n").append(values != null ? values.toString() : "-/-");
        }
        logResult(stringBuilder.toString());
    }

}
