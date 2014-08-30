package org.yellowbinary.server.basic_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.yellowbinary.server.basic_security.service.BasicUserDetailsService;

import java.util.Collections;

/**
* Created by jens on 28/08/14.
*/
@Configuration
@Order(2)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BasicUserDetailsService userDetailsService;

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ContentNegotiationStrategy negotiationStrategy = new HeaderContentNegotiationStrategy();
        MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(negotiationStrategy, MediaType.ALL);
        requestMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.APPLICATION_JSON));

        http.
                userDetailsService(userDetailsServiceBean()).
                requestMatcher(requestMatcher).

                antMatcher("/**/*").

                formLogin().
                loginProcessingUrl("/login").

                and().

                logout().
                logoutSuccessUrl("/logout").

                and().

                authorizeRequests().

                antMatchers("/login").permitAll().
                antMatchers(HttpMethod.POST, "/logout").authenticated().
                anyRequest().permitAll();

    }
}
