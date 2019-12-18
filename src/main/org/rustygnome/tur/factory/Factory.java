package org.rustygnome.tur.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Factory<T> {

    static private Map<Class, Factory> instances = new HashMap<>();
    private Class<T> type;

    static public <T> Factory<T> getInstance(Class<T> type) {
        if(instances.get(type) == null) {
            instances.put(type, new Factory<T>(type));
        }
        return instances.get(type);
    }

    static public <T> void setInstance(Class<T> type, Factory<T> mock) {
        instances.put(type, mock);
    }

    static public <T> void clearInstances() {
        instances = new HashMap<>();
    }

    public Factory(Class<T> type) {
        this.type = type;
    }

    public T createArtifact() {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException|IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
