package org.rustygnome.tur;

import org.rustygnome.tur.agent.Controller;
import org.rustygnome.tur.agent.Logger;

public class Application {

    static public String packageName = "???";
    static public String packageVersion = "???";

    static public void main(String[] args)
            throws Exception {

        Command command = Command
                .getInstance(null)
                .setupOptions()
                .processArgs(args);

        readPackageInformation();

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

    static void readPackageInformation() {
        Package applicationPackage = Application.class.getPackage();
        if (applicationPackage != null) {
            packageName = applicationPackage.getName();
            packageVersion = applicationPackage.getImplementationTitle();
        }
    }
}
