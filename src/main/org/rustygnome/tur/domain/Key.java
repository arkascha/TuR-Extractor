package org.rustygnome.tur.domain;

public enum Key {
    DATETIME(0, "Zeitpunkt"),
    NAME(1, "Name"),
    EMAIL(2, "E-Mail"),
    INTERESTS(3, "Interessen");

    private final int index;
    private final String name;

    Key(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public String getTitle() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
