package org.yellowbinary.server.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.yellowbinary.server.core.security.HeaderAuthenticationFilter;
import org.yellowbinary.server.core.security.HeaderAuthenticationUtil;
import org.yellowbinary.server.core.security.Security;
import org.yellowbinary.server.core.service.EncryptionService;
import org.yellowbinary.server.core.service.SessionService;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ACCESS_DENIED_JSON = "{\"message\":\"You are not privileged to request this resource.\", \"access-denied\":true,\"cause\":\"AUTHORIZATION_FAILURE\"}";
    private static final String UNAUTHORIZED_JSON = "{\"message\":\"Full authentication is required to access this resource.\", \"access-denied\":true,\"cause\":\"NOT AUTHENTICATED\"}";

    @Autowired
    private HeaderAuthenticationUtil headerAuthenticationUtil;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().

                withUser("user").password("password").roles("USER").

                and().

                withUser("admin").password("password").roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler().
                headerUtil(headerAuthenticationUtil).
                encryptionService(encryptionService).
                sessionService(sessionService);

        http.
                addFilterBefore(authenticationFilter(), LogoutFilter.class).

                csrf().disable().

                formLogin().successHandler(successHandler).
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
                antMatchers(HttpMethod.GET, "/**").hasRole("USER").
                antMatchers(HttpMethod.POST, "/**").hasRole("ADMIN").
                antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN").
                anyRequest().authenticated();

    }

    private Filter authenticationFilter() {
        return new HeaderAuthenticationFilter().
                userDetailsService(userDetailsService()).
                headerUtil(headerAuthenticationUtil).
                encryptionService(encryptionService);
    }

    private static class CustomAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            PrintWriter out = response.getWriter();
            out.print(ACCESS_DENIED_JSON);
            out.flush();
            out.close();

        }
    }

    private static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print(UNAUTHORIZED_JSON);
            out.flush();
            out.close();
        }
    }

    private static class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        private HeaderAuthenticationUtil headerAuthenticationUtil;
        private EncryptionService encryptionService;
        private SessionService sessionService;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws ServletException, IOException {

            String token  = headerAuthenticationUtil.setUserName(((User) authentication.getPrincipal()).getUsername());
            String timestamp = String.valueOf(sessionService.getTimestamp().getMillis());
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode().
                    put(Security.Header.TOKEN_KEY, encryptionService.encrypt(token)).
                    put(Security.Header.SESSION_KEY, encryptionService.encrypt(timestamp));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter out = response.getWriter();
            out.print(node.toString());
            out.flush();
            out.close();
            clearAuthenticationAttributes(request);
        }

        private CustomAuthenticationSuccessHandler headerUtil(HeaderAuthenticationUtil headerAuthenticationUtil) {
            this.headerAuthenticationUtil = headerAuthenticationUtil;
            return this;
        }

        public CustomAuthenticationSuccessHandler encryptionService(EncryptionService encryptionService) {
            this.encryptionService = encryptionService;
            return this;
        }

        public CustomAuthenticationSuccessHandler sessionService(SessionService sessionService) {
            this.sessionService = sessionService;
            return this;
        }
    }

}