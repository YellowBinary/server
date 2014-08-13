package org.yellowbinary.server.core.context;

import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

public class Context {

    private static final ThreadLocal<Context> contextThreadLocal = new ThreadLocal<>();

    public static Context current() {
        return contextThreadLocal.get();
    }

    public static void set(HttpServletRequest request, HttpServletResponse response) {
        if (current() == null) {
            contextThreadLocal.set(new Context(request, response));
        }
    }

    public static void clear() {
        if (current() != null) {
            current().attributes.clear();
            contextThreadLocal.remove();
        }
    }

    private final Map<String, Object> attributes = Maps.newHashMap();
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public Context(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
