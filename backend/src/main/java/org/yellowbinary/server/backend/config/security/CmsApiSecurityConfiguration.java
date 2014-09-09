package org.yellowbinary.server.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.yellowbinary.server.backend.security.HeaderAuthenticationFilter;
import org.yellowbinary.server.backend.security.HeaderAuthenticationUtil;
import org.yellowbinary.server.backend.security.ProviderBasedUserDetailsService;
import org.yellowbinary.server.backend.security.rest.RestAccessDeniedHandler;
import org.yellowbinary.server.backend.security.rest.RestAuthenticationEntryPoint;
import org.yellowbinary.server.backend.security.rest.RestAuthenticationFailureHandler;
import org.yellowbinary.server.backend.security.rest.RestAuthenticationSuccessHandler;
import org.yellowbinary.server.core.service.EncryptionUtil;
import org.yellowbinary.server.core.service.SessionUtil;

import javax.servlet.Filter;

/**
* Created by jens on 28/08/14.
*/
@Configuration
@Order(1)
public class CmsApiSecurityConfiguration extends AbstractCmsSecurityConfiguration {

    @Autowired
    private HeaderAuthenticationUtil headerAuthenticationUtil;

    @Autowired
    private EncryptionUtil encryptionUtil;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private ProviderBasedUserDetailsService userDetailsService;

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ContentNegotiationStrategy negotiationStrategy = new HeaderContentNegotiationStrategy();

        http.

                authenticationProvider(getAuthenticationProvider()).
                userDetailsService(userDetailsServiceBean()).
                requestMatcher(new MediaTypeRequestMatcher(negotiationStrategy, MediaType.APPLICATION_JSON)).

                antMatcher("/api/**/*").

                addFilterBefore(authenticationFilter(userDetailsService()), LogoutFilter.class).

                csrf().disable().

                formLogin().
                successHandler(successHandler()).
                failureHandler(failureHandler()).
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

    private DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    protected AuthenticationFailureHandler failureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    protected AuthenticationSuccessHandler successHandler() {
        return new RestAuthenticationSuccessHandler().
                encryptionService(encryptionUtil).
                headerUtil(headerAuthenticationUtil).
                sessionService(sessionUtil);
    }

    protected Filter authenticationFilter(UserDetailsService userDetailsService) {
        return new HeaderAuthenticationFilter().
                userDetailsService(userDetailsService).
                headerUtil(headerAuthenticationUtil);
    }

}
