package org.yellowbinary.server.core;

public class NoSuchProviderException extends RuntimeException {

    private final String base;
    private final String with;

    public NoSuchProviderException(String base, String with) {
        this.base = base;
        this.with = with;
    }

    public NoSuchProviderException(String s, String base, String with) {
        super(s);
        this.base = base;
        this.with = with;
    }

    public String getBase() {
        return base;
    }

    public String getWith() {
        return with;
    }
}
