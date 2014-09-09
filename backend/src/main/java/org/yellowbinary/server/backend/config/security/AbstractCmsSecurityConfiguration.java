package org.yellowbinary.server.backend.config.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@ComponentScan("org.yellowbinary.server.security.*")
@EnableJpaRepositories("org.yellowbinary.server.security.*.dao")
public class AbstractCmsSecurityConfiguration extends WebSecurityConfigurerAdapter {

}
