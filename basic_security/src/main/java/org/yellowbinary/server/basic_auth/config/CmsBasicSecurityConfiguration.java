package org.yellowbinary.server.basic_auth.config;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.hibernate4.encryptor.HibernatePBEEncryptorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.PostConstruct;

@Configuration
@Import(CustomGlobalMethodSecurityConfiguration.class)
public class CmsBasicSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @PostConstruct
    protected void configure() throws Exception {

        String applicationSecret = env.getProperty("application.secret");

        PBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(applicationSecret);
        HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
        registry.registerPBEStringEncryptor("stringEncryptor", encryptor);

    }

}