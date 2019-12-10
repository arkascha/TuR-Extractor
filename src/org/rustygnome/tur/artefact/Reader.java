package org.rustygnome.tur.artefact;

import org.apache.commons.codec.DecoderException;
import org.rustygnome.tur.factory.Factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class Reader {

    static public Reader getInstance()
            throws InstantiationException, IllegalAccessException {
        return Factory.getInstance(Reader.class).createArtefact();
    }

    public String read(InputStreamReader inputStream)
            throws IOException, DecoderException {
        BufferedReader inputReader = new BufferedReader(inputStream);
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
