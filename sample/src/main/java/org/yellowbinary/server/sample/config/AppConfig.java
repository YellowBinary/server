package org.yellowbinary.server.sample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.yellowbinary.server.backend.config.security.EnableCmsApiSecurity;
import org.yellowbinary.server.backend.config.EnableCms;
import org.yellowbinary.server.preview.config.EnableCmsPreview;
import org.yellowbinary.server.sample.SampleDataFixtures;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource({"classpath:config.properties"})
@EnableTransactionManagement
@EnableCms
@EnableCmsApiSecurity()
@EnableCmsPreview
@Import(DataConfig.class)
public class AppConfig {

    @Autowired
    private SampleDataFixtures sampleDataFixtures;

    @PostConstruct
    public void init() {
        sampleDataFixtures.create();
    }
}
