package org.yellowbinary.cms.server.core.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yellowbinary.cms.server.core.model.Configuration;

import javax.persistence.EntityManager;

@Repository
public class ConfigurationDaoImpl implements ConfigurationDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public <T> T readValue(Class<T> type, String name) {
        return readValue(type, name, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readValue(Class<T> type, String name, T defaultValue) {

        Configuration configuration = getConfiguration(name);

        if (configuration == null) {
            return defaultValue;
        }

        String value = configuration.getValue();
        if (type.getClass().equals(Integer.class)) {
            if (value != null) {
                return (T) Integer.valueOf(value);
            }
            return defaultValue;
        }
        if (type.getClass().equals(Long.class)) {
            if (value != null) {
                return (T)Long.valueOf(value);
            }
            return defaultValue;
        }
        if (type.getClass().equals(Boolean.class)) {
            if (value != null) {
                return (T)Boolean.valueOf(value);
            }
            return defaultValue;
        }
        if (type.getClass().equals(Float.class)) {
            if (value != null) {
                return (T)Float.valueOf(value);
            }
            return defaultValue;
        }
        if (type.getClass().equals(Double.class)) {
            if (value != null) {
                return (T)Double.valueOf(value);
            }
            return defaultValue;
        }
        throw new RuntimeException(String.format("Unknown type [%s]", type));
    }

    private Configuration getConfiguration(String name) {
        return (Configuration)entityManager.
                    createQuery("SELECT c FROM Configuration WHERE name=:name").
                    setParameter("name", name).getSingleResult();
    }

    @Override
    public <T> void updateValue(String name, T value) {

        Configuration configuration = getConfiguration(name);
        if (configuration != null) {
            configuration.setValue(String.valueOf(value));
            entityManager.merge(configuration);
            return;
        }

        Configuration c = new Configuration();
        c.setName(name);
        c.setValue(String.valueOf(value));
        entityManager.persist(c);
    }

    @Override
    public <T> void setValueIfMissing(String name, T value) {

        Configuration configuration = getConfiguration(name);
        if (configuration == null) {
            Configuration c = new Configuration();
            c.setName(name);
            c.setValue(String.valueOf(value));
            entityManager.persist(c);
        }

    }
}
