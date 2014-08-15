package org.yellowbinary.server.core.security;

public final class Security {

    private Security() {
    }

    public static class Header {
        public static final String SESSION_KEY = "X-Session-Timestamp";
        public static final String TOKEN_KEY = "X-Auth-Token";
    }

    public static class Params {
        public static final String AUTH_HANDLER = "authorization_handler";
        public static final String AUTH_META = "authorization_meta";
        public static final String AUTH_PATH = "authorization_path";
        public static final String AUTH_USERNAME = "authentication_username";
        public static final String AUTH_PASSWORD = "authentication_password";
        public static final String AUTH_USER = "authentication_user";
    }

    public static class Types {
        public static final String RESOURCE = "authorization.type.resource";
    }

}
