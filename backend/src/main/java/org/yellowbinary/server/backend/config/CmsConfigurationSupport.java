package org.yellowbinary.server.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.yellowbinary.server.backend.dao.ConfigurationDao;
import org.yellowbinary.server.backend.security.HeaderAuthenticationUtil;
import org.yellowbinary.server.core.service.EncryptionUtil;
import org.yellowbinary.server.core.service.SessionUtil;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories({"org.yellowbinary.server.backend.dao", "org.yellowbinary.server.backend.dao.event"})
@ComponentScan({"org.yellowbinary.server.core", "org.yellowbinary.server.backend"})
public class CmsConfigurationSupport {

    private static final Logger LOG = LoggerFactory.getLogger(CmsConfigurationSupport.class);

    @Autowired
    private ConfigurationDao configurationDao;

    private EncryptionUtil encryptionUtil;
    private SessionUtil sessionUtil;

    @Bean
    public SessionUtil getSessionUtil() {
        if (sessionUtil == null) {
            String maxAge = configurationDao.readValue(String.class, "application.session.maxAge", "30m");
            sessionUtil = new SessionUtil().sessionMaxAge(maxAge).encryptionUtil(getEncryptionUtil());
        }
        return sessionUtil;
    }

    @Bean
    public EncryptionUtil getEncryptionUtil() {
        if (encryptionUtil == null) {
            String secret = configurationDao.readValue(String.class, "application.encryption.secret", String.valueOf(hashCode()));
            Boolean encryption = configurationDao.readValue(Boolean.class, "application.encryption.enabled", true);
            encryptionUtil = new EncryptionUtil().encryptionEnabled(encryption).applicationSecret(secret);
        }
        return encryptionUtil;
    }
}
