package org.rustygnome.tur;

import org.rustygnome.tur.agent.Controller;
import org.rustygnome.tur.agent.Logger;
import org.rustygnome.tur.factory.Factored;

public class Application
        extends Factored {

    static final String TAG = Application.class.getSimpleName();

    static public String packageTitle = "???";
    static public String packageVersion = "???";

    static public Application getInstance() {
        return (Application) Factored.getInstance(Application.class);
    }

    static public void main(String[] args) {
        try {
            getInstance().startUp(args);
        } catch (Exception e) {
            Logger.getInstance().logException(TAG, e);
        }
    }

    public Application() {
        super();
    }

    void startUp(String[] args) {
        Command.getInstance().setupOptions().processArgs(args);
        Logger.getInstance().logDebug(TAG, "application starting up");

        readPackageInformation(Application.class.getPackage());

        try {
            Logger.getInstance().logDebug(TAG, "creating controller");
            Controller controller = Factored.getFactory(Controller.class).createArtifact();
            controller.run();
        } catch(Exception e) {
            if (Command.hasOption("action")) {
                Logger.getInstance().logResult("FAILURE");
            }
            Logger.getInstance().logException(TAG, e);
        }
    }

    void readPackageInformation(Package applicationPackage) {
        Logger.getInstance().logDebug(TAG, "reading package information");
        if (applicationPackage != null) {
            packageTitle = applicationPackage.getImplementationTitle();
            packageVersion = applicationPackage.getImplementationVersion();
        }
    }
}
