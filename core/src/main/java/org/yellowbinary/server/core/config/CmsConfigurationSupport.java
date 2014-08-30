package org.yellowbinary.server.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("org.yellowbinary.server.core.dao")
@ComponentScan("org.yellowbinary.server.core")
public class CmsConfigurationSupport {
}
