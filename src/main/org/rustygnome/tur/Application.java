package org.rustygnome.tur;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.codec.DecoderException;
import org.jetbrains.annotations.Nullable;
import org.rustygnome.tur.agent.Controller;
import org.rustygnome.tur.agent.Logger;
import org.rustygnome.tur.factory.Factored;
import org.rustygnome.tur.factory.Factory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Application
        extends Factored {

    static public String packageTitle = "???";
    static public String packageVersion = "???";

    static public Factory<Application> getFactory() {
        return Factory.getInstance(Application.class);
    }

    static public Application getInstance()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return getFactory().createArtifact(null);
    }

    static public void main(String[] args)
            throws Exception {
        getInstance().startUp(args);
    }

    public Application(Command command) {
        super(command);
        readPackageInformation(Application.class.getPackage());
    }

    void startUp(String[] args)
            throws Exception {

        Command command = Command
                .getInstance()
                .setupOptions()
                .processArgs(args);

        try {
            Controller controller = Controller.getFactory().createArtifact(command);
            controller.run();
        } catch(Exception e) {
            if (command.hasOption("action")) {
                Logger.getFactory().createArtifact(command).log("FAILURE");
            }
            throw e;
        }
    }

    void readPackageInformation(Package applicationPackage) {
        if (applicationPackage != null) {
            packageTitle = applicationPackage.getImplementationTitle();
            packageVersion = applicationPackage.getImplementationVersion();
        }
    }
}
