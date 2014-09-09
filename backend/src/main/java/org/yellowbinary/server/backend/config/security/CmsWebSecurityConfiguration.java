package org.yellowbinary.server.backend.config.security;

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
import org.yellowbinary.server.backend.security.ProviderBasedUserDetailsService;

import java.util.Collections;

@Configuration
@Order(2)
public class CmsWebSecurityConfiguration extends AbstractCmsSecurityConfiguration {

    @Autowired
    private ProviderBasedUserDetailsService userDetailsService;

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
