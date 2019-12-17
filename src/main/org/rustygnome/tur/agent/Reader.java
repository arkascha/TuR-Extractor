package org.rustygnome.tur.agent;

import org.apache.commons.codec.DecoderException;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.factory.Factored;
import org.rustygnome.tur.factory.Factory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class Reader
        extends Factored {

    static public Factory<Reader> getFactory() {
        return Factory.getInstance(Reader.class);
    }

    static public Reader getInstance(Command command)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return getFactory().createArtifact(command);
    }

    public Reader(Command command) {
        super(command);
    }

    public String read(InputStream inputStream)
            throws IOException, DecoderException {

        if (inputStream != null) {
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            CharBuffer inputBuffer = CharBuffer.allocate(10000);
            inputReader.read(inputBuffer);
            inputBuffer.flip();
//            return decodeInput(inputBuffer.toString());
            return inputBuffer.toString();
        }
        return null;
    }

    private String decodeInput(String string)
            throws UnsupportedEncodingException {
        string = string.replaceAll("=", "%");
        string = java.net.URLDecoder.decode(string, StandardCharsets.UTF_8.name());
        return string;
    }
}
