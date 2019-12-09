package org.rustygnome.rut;

import com.sun.istack.internal.NotNull;

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

    public Factory(@NotNull Class<T> type) {
        this.type = type;
    }

    public T create() throws IllegalAccessException, InstantiationException {
        return type.newInstance();
    }
}
