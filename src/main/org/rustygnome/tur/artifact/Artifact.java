package org.rustygnome.tur.artifact;

import org.rustygnome.tur.Command;

abstract public class Artifact {

    protected Command command;

    public Artifact(Command command) {
        this.command = command;
    }
}
