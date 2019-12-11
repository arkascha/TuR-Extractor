package org.rustygnome.tur.artefact;

import org.apache.commons.codec.DecoderException;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.factory.Factory;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class Reader
        extends Artefact {

    static public Factory<Reader> getFactory() {
        return Factory.getInstance(Reader.class);
    }

    public Reader(Command command) {
        super(command);
    }

    public String read(InputStreamReader inputReader)
            throws IOException, DecoderException {

        CharBuffer inputBuffer = CharBuffer.allocate(10000);
        inputReader.read(inputBuffer);
        inputBuffer.flip();
//        return decodeInput(inputBuffer.toString());
        return inputBuffer.toString();
    }

    private String decodeInput(String string)
            throws UnsupportedEncodingException {
        string = string.replaceAll("=", "%");
        string = java.net.URLDecoder.decode(string, StandardCharsets.UTF_8.name());
        return string;
    }
}
