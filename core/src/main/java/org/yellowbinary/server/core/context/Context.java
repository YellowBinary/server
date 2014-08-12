package org.yellowbinary.server.core.context;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

public class Context {

    private static final ThreadLocal<Context> contextThreadLocal = new ThreadLocal<>();

    public static Context current() {
        return contextThreadLocal.get();
    }

    public static void set() {
        if (current() == null) {
            contextThreadLocal.set(new Context());
        }
    }

    public static void clear() {
        if (current() != null) {
            current().attributes.clear();
            contextThreadLocal.remove();
        }
    }

    Map<String, Object> attributes = Maps.newHashMap();

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }
}
