package org.yellowbinary.server.basic_auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.yellowbinary.server.basic_auth.service.BasicUserDetailsService;
import org.yellowbinary.server.basic_auth.service.HeaderAuthenticationFilter;
import org.yellowbinary.server.basic_auth.service.HeaderAuthenticationUtil;
import org.yellowbinary.server.basic_auth.service.rest.RestAccessDeniedHandler;
import org.yellowbinary.server.basic_auth.service.rest.RestAuthenticationEntryPoint;
import org.yellowbinary.server.basic_auth.service.rest.RestAuthenticationSuccessHandler;
import org.yellowbinary.server.core.service.EncryptionService;
import org.yellowbinary.server.core.service.SessionService;

import javax.servlet.Filter;

/**
* Created by jens on 28/08/14.
*/
@Configuration
@Order(1)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BasicUserDetailsService userDetailsService;

    @Autowired
    private HeaderAuthenticationUtil headerAuthenticationUtil;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private SessionService sessionService;

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ContentNegotiationStrategy negotiationStrategy = new HeaderContentNegotiationStrategy();

        http.
                userDetailsService(userDetailsServiceBean()).
                requestMatcher(new MediaTypeRequestMatcher(negotiationStrategy, MediaType.APPLICATION_JSON)).

                antMatcher("/api/**/*").

                addFilterBefore(authenticationFilter(userDetailsService()), LogoutFilter.class).

                csrf().disable().

                formLogin().successHandler(successHandler()).
                loginProcessingUrl("/api/login").

                and().

                logout().
                logoutSuccessUrl("/api/logout").

                and().

                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).

                and().

                exceptionHandling().
                accessDeniedHandler(new RestAccessDeniedHandler()).
                authenticationEntryPoint(new RestAuthenticationEntryPoint()).

                and().

                authorizeRequests().

                antMatchers(HttpMethod.POST, "/api/login").permitAll().
                antMatchers(HttpMethod.POST, "/api/logout").authenticated().
                anyRequest().permitAll();
    }

    protected AuthenticationSuccessHandler successHandler() {
        return new RestAuthenticationSuccessHandler().
                encryptionService(encryptionService).
                headerUtil(headerAuthenticationUtil).
                sessionService(sessionService);
    }

    protected Filter authenticationFilter(UserDetailsService userDetailsService) {
        return new HeaderAuthenticationFilter().
                userDetailsService(userDetailsService).
                headerUtil(headerAuthenticationUtil);
    }

}
