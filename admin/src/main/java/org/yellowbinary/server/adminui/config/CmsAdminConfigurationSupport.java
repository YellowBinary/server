package org.yellowbinary.server.adminui.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan(basePackages = {"org.yellowbinary.server.adminui"})
@Configuration
public class CmsAdminConfigurationSupport {


}
