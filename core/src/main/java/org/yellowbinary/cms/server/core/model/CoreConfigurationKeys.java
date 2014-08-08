package org.yellowbinary.cms.server.core.model;

public final class CoreConfigurationKeys {

    public static final String BASE_URL = "base_url";
    public static final String START_PAGE = "start_page";
    public static final String PAGE_NOT_FOUND_PAGE = "page_not_found_page";
    public static final String INTERNAL_SERVER_ERROR_PAGE = "internal_server_error_page";
    public static final String UNAUTHORIZED_PAGE = "unauthorized_page";
    public static final String LOGIN_PAGE = "login_page";

    public static final String THEME = "theme";
    public static final String THEME_VARIANT = "theme_variant";
    public static final String NAVIGATION_TYPE = "navigation_type";

    public static final String DEFAULT_FORM_TYPE = "event.default_form_type";
    public static final String SUBMIT_HANDLER = "event.submit_handler";
    public static final String VALIDATION_HANDLER = "event.validation_processing_handler";

    public static final String USER_TYPE = "origo.authentication.user_type";

    public static final String PREVIEW_TICKET_PERIOD = "origo.preview.ticket_period";

    public static final String INPUT_SUPPRESS_PASSWORD_VALUE = "origo.input.suppress_password_value";

    private CoreConfigurationKeys() {
    }
}
