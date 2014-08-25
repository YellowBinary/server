package org.yellowbinary.server.basic_auth.config;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.hibernate4.encryptor.HibernatePBEEncryptorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.yellowbinary.server.basic_auth.service.BasicUserDetailsService;
import org.yellowbinary.server.core.security.CustomAccessDeniedHandler;
import org.yellowbinary.server.core.security.CustomAuthenticationEntryPoint;
import org.yellowbinary.server.core.security.CustomAuthenticationSuccessHandler;
import org.yellowbinary.server.core.security.HeaderAuthenticationFilter;
import org.yellowbinary.server.core.security.HeaderAuthenticationUtil;
import org.yellowbinary.server.core.service.EncryptionService;
import org.yellowbinary.server.core.service.SessionService;

import javax.servlet.Filter;

@Configuration
@Import(CustomGlobalMethodSecurityConfiguration.class)
public class BasicSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private HeaderAuthenticationUtil headerAuthenticationUtil;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private BasicUserDetailsService userDetailsService;

    @Autowired
    private Environment env;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        String applicationSecret = env.getProperty("application.secret");

        PBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(applicationSecret);
        HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
        registry.registerPBEStringEncryptor("stringEncryptor", encryptor);


        auth.userDetailsService(userDetailsService);
/*

        auth.inMemoryAuthentication().

                withUser("user").password("password").roles("USER").

                and().

                withUser("admin").password("password").roles("USER", "ADMIN");
*/
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                addFilterBefore(authenticationFilter(userDetailsService()), LogoutFilter.class).

                csrf().disable().

                formLogin().successHandler(successHandler()).
                loginProcessingUrl("/login").

                and().

                logout().
                logoutSuccessUrl("/logout").

                and().

                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).

                and().

                exceptionHandling().
                accessDeniedHandler(new CustomAccessDeniedHandler()).
                authenticationEntryPoint(new CustomAuthenticationEntryPoint()).

                and().

                authorizeRequests().

                antMatchers(HttpMethod.POST, "/login").permitAll().
                antMatchers(HttpMethod.POST, "/logout").authenticated().
                anyRequest().permitAll();

    }

    protected AuthenticationSuccessHandler successHandler() {
        return new CustomAuthenticationSuccessHandler().
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