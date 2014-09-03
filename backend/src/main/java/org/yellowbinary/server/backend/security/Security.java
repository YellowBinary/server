package org.yellowbinary.server.backend.security;

public final class Security {

    private Security() {
    }

    public static class With {
        public static final String AUTHORIZATION_SUBJECT = "authorization_subject";
        public static final String AUTHORIZATION_FAILURE = "authorization_failure";
        public static final String AUTHORIZATION_CHECK = "authorization_check";
        public static final String AUTHORIZATION_ROLES = "authorization_roles";
        public static final String AUTHORIZATION_ACCESS_DECISION_MANAGER = "authorization_access_decision_manager";
        public static final String AUTHORIZATION = "authorization";

        public static final String AUTHENTICATION_CHECK = "authentication_check";
        public static final String AUTHENTICATION_CURRENT_USER = "authentication_current_user";
        public static final String AUTHENTICATION_VALIDATE = "authentication_validate";
        public static final String AUTHENTICATION_SIGNOUT = "authentication_signout";
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

}
