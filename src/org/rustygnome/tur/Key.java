package org.rustygnome.tur;

public enum Key {
    NAME("Name"),
    EMAIL("E-Mail"),
    INTERESTS("Interessen"),
    DATETIME("Zeitpunkt");

    private String name;

    Key(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
