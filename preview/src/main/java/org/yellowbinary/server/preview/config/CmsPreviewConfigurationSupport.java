package org.yellowbinary.server.preview.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("org.yellowbinary.server.preview.dao")
@ComponentScan("org.yellowbinary.server.preview")
public class CmsPreviewConfigurationSupport {
}
