package org.yellowbinary.server.core;

public class InterceptorException extends Exception {

    private final String module;

    public InterceptorException(String module) {
        super();
        this.module = module;
    }

    public InterceptorException(String module, String message) {
        super(message);
        this.module = module;
    }

    public InterceptorException(String module, String message, Throwable cause) {
        super(message, cause);
        this.module = module;
    }

    public InterceptorException(String module, Throwable cause) {
        super(cause);
        this.module = module;
    }

    protected InterceptorException(String module, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.module = module;
    }

    public String getModule() {
        return module;
    }
}
