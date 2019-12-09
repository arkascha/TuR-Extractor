package org.rustygnome.tur;

import com.sun.istack.internal.NotNull;
import org.apache.commons.collections4.list.TreeList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Values {

    private final Map<Key, String> values = new TreeMap<Key, String>() {{
        put(Key.NAME, null);
        put(Key.EMAIL, null);
        put(Key.INTERESTS, null);
        put(Key.DATETIME, null);
    }};

    static public Values getTitles() {
        Values titles = new Values();
        Iterator<Values.Entry> iterator = titles.entrySet().iterator();
        while (iterator.hasNext()) {
            Values.Entry entry = iterator.next();
            titles.put(entry.getKey(), entry.getKey().toString());
        }
        return titles;
    }

    public void put(@NotNull Key key, @NotNull String value) {
        values.put(key, value);
    }

    public String get(@NotNull Key key) {
        return values.get(key);
    }

    public String[] toArray() {
        return values.values().toArray(new String[0]);
    }

    public List<Entry> entrySet() {
        List<Entry> set = new TreeList<>();

        for (Map.Entry<Key, String> keyStringEntry : values.entrySet()) {
            set.add(new Entry(keyStringEntry));
        }

        return set;
    }

    // =====

    static public class Entry {

        private Key key;
        private String value;

        Entry(Map.Entry<Key, String> entry) {
            this.key = entry.getKey();
            this.value = entry.getValue();
        }

        public Key getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String values) {
            this.value = value;
        }
    }
}