package org.yellowbinary.server.backend.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ConfigurationDao {

    <T> T readValue(Class<T> type, String name);

    <T> T readValue(Class<T> type, String name, T defaultValue);

    <T> void updateValue(String name, T value);

    <T> void setValueIfMissing(String name, T value);

}
