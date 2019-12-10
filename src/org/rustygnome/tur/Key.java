package org.rustygnome.tur;

public enum Key {
    DATETIME(0, "Zeitpunkt"),
    NAME(1, "Name"),
    EMAIL(2, "E-Mail"),
    INTERESTS(3, "Interessen");


    private int index;
    private String name;

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
