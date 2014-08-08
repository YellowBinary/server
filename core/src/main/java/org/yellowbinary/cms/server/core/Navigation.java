package org.yellowbinary.cms.server.core;

public interface Navigation<T> {

    String getReferenceId();

    String getSection();

    int getWeight();

    String type();
}
