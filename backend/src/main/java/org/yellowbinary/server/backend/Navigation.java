package org.yellowbinary.server.backend;

public interface Navigation<T> {

    String getReferenceId();

    String getSection();

    int getWeight();

    String type();
}
