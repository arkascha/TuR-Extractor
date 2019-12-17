package org.rustygnome.tur.factory;

import org.rustygnome.tur.Command;

abstract public class Factored {

    protected Command command;

    public Factored(Command command) {
        this.command = command;
    }
}
