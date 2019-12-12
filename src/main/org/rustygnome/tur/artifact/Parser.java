package org.rustygnome.tur.artifact;

import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Key;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser
        extends Artifact {

    private final String DATE_PATTERN = "yyyy-MM'= =2D'dd HH:mm:ss";
    private final String DATE_FORMAT = "dd.MM.yyyy HH:mm";
    private final String EXTRACTION_REGEX =
        "\\s+Name, Vorname:(.*)$" +
        "\\s+E-Mail:(.*)$" +
        "\\s+Meine Hauptinteressen sind\\s+\\.+:(.*)$" +
        "\\s+Nutzer hat die Datenschutzerkl=C3=A4rung akzeptiert\\." +
        "\\s+Datum/Uhrzeit:(.+)CET\\s+$";

    private Values values = new Values();

    static public Factory<Parser> getFactory() {
        return Factory.getInstance(Parser.class);
    }

    public Parser(Command command) {
        super(command);
    }

    public Values parse(String message) {

        if (message != null) {
            final Pattern pattern = Pattern.compile(EXTRACTION_REGEX, Pattern.MULTILINE | Pattern.DOTALL);
            final Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                values.put(Key.NAME, normalizeString(matcher.group(1)));
                values.put(Key.EMAIL, normalizeString(matcher.group(2)));
                values.put(Key.INTERESTS, normalizeString(matcher.group(3)));
                values.put(Key.DATETIME, normalizeDatetime(normalizeString(matcher.group(4))));
                return values;
            }
            throw new RuntimeException("Failed to parse message");
        }

        return null;
    }

    private String normalizeString(String string) {
        return string.trim().replaceAll("\n", " ");
    }

    private String normalizeDatetime(String datetime) {

        if (datetime != null) {
            try {
                LocalDateTime dateTime = LocalDateTime.from(DateTimeFormatter.ofPattern(DATE_PATTERN).parse(datetime));
                return dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
            } catch (DateTimeParseException e) { }
        }

        return datetime;
    }
}
