package org.yellowbinary.cms.server.standalone.config;

import org.yellowbinary.cms.server.core.config.EnableCms;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableCms
public class AppConfig {
}
