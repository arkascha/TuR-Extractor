package org.rustygnome.tur.factory;

import java.util.HashMap;
import java.util.Map;

abstract public class Factored<T> {

    static private Map<Class, Factored> instances = new HashMap<>();

    static public <T> Factory<T> getFactory(Class<T> type) {
        return Factory.getInstance(type);
    }

    static public <T> Factored<T> getInstance(Class<T> type) {
        if (instances.get(type) ==null) {
            instances.put(type, (Factored) getFactory(type).createArtifact());
        }
        return instances.get(type);
    }

    static public <T> void setInstance(Class<T> type, Factored<T> mock) {
        instances.put(type, mock);
    }

    static public <T> void clearInstances() {
        instances = new HashMap<>();
    }
}
