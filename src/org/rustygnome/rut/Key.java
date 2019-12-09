package org.rustygnome.rut;

import com.sun.istack.internal.NotNull;

public enum Key {
    NAME("Name"),
    EMAIL_ADDRESS("E-Mail"),
    INTERESTS("Interessen"),
    DATETIME("Zeitpunkt");

    private String name;

    Key(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    static public Key byString(@NotNull String name) {
        for (Key key : values()) {
            if (key.toString().equals(name)) {
                return key;
            }
        }
        return null;
    }
}
