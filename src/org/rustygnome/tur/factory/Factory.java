package org.rustygnome.tur.factory;

import com.sun.istack.internal.NotNull;
import org.rustygnome.tur.Command;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Factory<T> {

    static private Map<Class, Factory> instances = new HashMap<>();
    private Class<T> type;

    static public <T> Factory<T> getInstance(@NotNull Class<T> type) {
        if(instances.get(type) == null) {
            instances.put(type, new Factory<T>(type));
        }
        return instances.get(type);
    }

    static public <T> void setInstance(@NotNull Class<T> type, @NotNull Factory<T> mock) {
        instances.put(type, mock);
    }

    static public <T> void clearInstances() {
        instances = new HashMap<>();
    }

    public Factory(@NotNull Class<T> type) {
        this.type = type;
    }

    public T createArtefact(Command command)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return type.getDeclaredConstructor(Command.class).newInstance(command);
    }
}
