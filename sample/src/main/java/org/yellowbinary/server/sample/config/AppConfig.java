package org.yellowbinary.server.sample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.yellowbinary.server.basic_auth.config.EnableCmsBasicSecurity;
import org.yellowbinary.server.core.config.EnableCms;
import org.yellowbinary.server.preview.config.EnableCmsPreview;
import org.yellowbinary.server.sample.SampleDataFixtures;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource({"classpath:config.properties"})
@EnableTransactionManagement
@EnableCms
@EnableCmsBasicSecurity
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
