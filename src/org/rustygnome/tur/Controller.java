package org.rustygnome.tur;

import com.sun.istack.internal.NotNull;
import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;

public class Controller {

    private Writer writer;
    private Parser parser;
    private Reader reader;

    static public Controller getInstance() throws IllegalAccessException, InstantiationException {
        return Factory.getInstance(Controller.class).create();
    }

    public Controller() throws IllegalAccessException, InstantiationException {
        writer = Writer.getInstance();
        parser = Parser.getInstance();
        reader = Reader.getInstance();
    }

    public void run() throws IOException, DecoderException {
        String message = reader.readInput();
        Values values = parser.extract(message);
        boolean written = writer.write(values);
        log(written, values);
    }

    public void log(boolean processed, @NotNull Values values) {
        System.out.println(
                String.format(
                        "[%s %s] %s: \n%s\n\n",
                        LocalDate.now(),
                        LocalTime.now(),
                        processed ? "PROCESSED" : "IGNORED",
                        formatValues(values)
                ));
    }

    private String formatValues(@NotNull Values values) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Values.Entry> iterator = values.entrySet().iterator();
        while (iterator.hasNext()) {
            Values.Entry entry = iterator.next();
            stringBuilder.append(
                    String.format(
                            "%s: %s",
                            entry.getKey().toString(),
                            entry.getValue()));
            if (iterator.hasNext()) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
