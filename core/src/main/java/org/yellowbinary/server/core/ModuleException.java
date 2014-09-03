package org.yellowbinary.server.core;

public class ModuleException extends Exception {

    private final String name;
    private final Reason reason;

    public ModuleException(String name, Reason reason) {
        super();
        this.name = name;
        this.reason = reason;
    }

    public ModuleException(String name, Reason reason, String message) {
        super(message);
        this.name = name;
        this.reason = reason;
    }

    public ModuleException(String name, Reason reason, String message, Throwable throwable) {
        super(message, throwable);
        this.name = name;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public Reason getExceptionCause() {
        return reason;
    }

    public static enum Reason {
        NOT_INITIALIZED, NOT_INSTALLED, NOT_ENABLED
    }
}
