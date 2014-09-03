package org.yellowbinary.server.security;

public class IdentityUnavailableException extends Throwable {

    public IdentityUnavailableException() {
        super();
    }

    public IdentityUnavailableException(String message) {
        super(message);
    }

    public IdentityUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdentityUnavailableException(Throwable cause) {
        super(cause);
    }

    protected IdentityUnavailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
