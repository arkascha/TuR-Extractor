package org.rustygnome.tur.artefact;

import org.rustygnome.tur.Command;

abstract public class Artefact {

    protected Command command;

    public Artefact(Command command) {
        this.command = command;
    }
}
