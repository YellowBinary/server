package org.yellowbinary.server.core.service;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.hibernate4.encryptor.HibernatePBEEncryptorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.core.dao.ConfigurationDao;

import javax.annotation.PostConstruct;

@Service
public class EncryptionService {

    private static final Logger LOG = LoggerFactory.getLogger(EncryptionService.class);

    @Value(value = "${application.secret}")
    private String applicationSecret;

    @Autowired
    private ConfigurationDao configurationDao;

    private PBEStringEncryptor encryptor;
    private boolean useEncryption = true;

    @PostConstruct
    public void register() {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(applicationSecret);
        useEncryption = configurationDao.readValue(Boolean.class, "application.session.encryption", true);
        LOG.debug("Using Encryption: " + useEncryption);

        HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
        registry.registerPBEStringEncryptor("strongHibernateStringEncryptor", encryptor);
    }

    public String decrypt(String value) {
        if (useEncryption && !StringUtils.isBlank(value)) {
            return encryptor.decrypt(value);
        }
        return value;
    }

    public String encrypt(String value) {
        if (useEncryption && !StringUtils.isBlank(value)) {
            return encryptor.encrypt(value);
        }
        return value;
    }

}
