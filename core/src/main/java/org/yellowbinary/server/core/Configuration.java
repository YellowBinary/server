package org.yellowbinary.server.core;

public interface Configuration {

    <T> T readValue(Class<T> type, String name);

    <T> T readValue(Class<T> type, String name, T defaultValue);

    <T> void updateValue(String name, T value);

    <T> void setValueIfMissing(String name, T value);

}
