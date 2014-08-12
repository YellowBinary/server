package org.yellowbinary.server.core.dao;

import org.springframework.stereotype.Repository;
import org.yellowbinary.server.core.model.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ConfigurationDaoImpl implements ConfigurationDao {

    @PersistenceContext
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
        if (type.equals(String.class)) {
            if (value != null) {
                return (T)value;
            }
            return defaultValue;
        }
        if (type.equals(Integer.class)) {
            if (value != null) {
                return (T) Integer.valueOf(value);
            }
            return defaultValue;
        }
        if (type.equals(Long.class)) {
            if (value != null) {
                return (T)Long.valueOf(value);
            }
            return defaultValue;
        }
        if (type.equals(Boolean.class)) {
            if (value != null) {
                return (T)Boolean.valueOf(value);
            }
            return defaultValue;
        }
        if (type.equals(Float.class)) {
            if (value != null) {
                return (T)Float.valueOf(value);
            }
            return defaultValue;
        }
        if (type.equals(Double.class)) {
            if (value != null) {
                return (T)Double.valueOf(value);
            }
            return defaultValue;
        }
        throw new RuntimeException(String.format("Unknown type [%s]", type));
    }

    private Configuration getConfiguration(String name) {
        try {
            return (Configuration)entityManager.
                        createQuery("SELECT c FROM Configuration c WHERE c.name=:name").
                        setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
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
