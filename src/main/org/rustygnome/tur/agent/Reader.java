package org.rustygnome.tur.agent;

import org.rustygnome.tur.factory.Factored;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class Reader
        extends Factored {

    static final String TAG = Reader.class.getSimpleName();

    static public Reader getInstance() {
        return (Reader) Factored.getInstance(Reader.class);
    }

    public Reader() {
        super();
    }

    public String read(InputStream inputStream) {
        Logger.getInstance().logDebug(TAG, "reading input");
        if (inputStream != null) {
            CharBuffer inputBuffer = CharBuffer.allocate(10000);
            try {
                new InputStreamReader(inputStream).read(inputBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            inputBuffer.flip();
            Logger.getInstance().logDebug(TAG, "read input of length " + inputBuffer.length());
//            return decodeInput(inputBuffer.toString());
            return inputBuffer.toString();
        }
        return null;
    }

    private String decodeInput(String string) {
        string = string.replaceAll("=", "%");
        try {
            string = java.net.URLDecoder.decode(string, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return string;
    }
}
