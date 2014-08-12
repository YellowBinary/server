package org.yellowbinary.server.core.model;

import java.util.Map;

public class Component {

    public static final String PATH_ALIAS = "PATH_ALIAS";
    public static final String TYPE = "component";

    public Map<String, String> headers;
    public String body;

    private Component() {}

    public Component(Map<String, String> headers, String body) {
        this.headers = headers;
        this.body = body;
    }

}
