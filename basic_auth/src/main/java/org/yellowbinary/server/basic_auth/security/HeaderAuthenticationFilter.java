package org.yellowbinary.server.basic_auth.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;
import org.yellowbinary.server.core.service.EncryptionService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class HeaderAuthenticationFilter extends GenericFilterBean {

    private UserDetailsService userDetailsService;

    private HeaderAuthenticationUtil headerAuthenticationUtil;

    private EncryptionService encryptionService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        UserDetails userDetails = loadUserDetails();
        SecurityContext contextBeforeChainExecution = createSecurityContext(userDetails);

        try {
            SecurityContextHolder.setContext(contextBeforeChainExecution);
            if (contextBeforeChainExecution.getAuthentication() != null && contextBeforeChainExecution.getAuthentication().isAuthenticated()) {
                String userName = (String) contextBeforeChainExecution.getAuthentication().getPrincipal();
                headerAuthenticationUtil.setUserName(userName);
            }
            filterChain.doFilter(request, response);
        }
        finally {
            // Clear the context and free the thread local
            SecurityContextHolder.clearContext();
        }
    }

    private SecurityContext createSecurityContext(UserDetails userDetails) {
        if (userDetails != null) {
            SecurityContextImpl securityContext = new SecurityContextImpl();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            securityContext.setAuthentication(authentication);
            return securityContext;
        }
        return SecurityContextHolder.createEmptyContext();
    }

    private UserDetails loadUserDetails() {
        String userName = headerAuthenticationUtil.getUserName();

        return userName != null
                ? userDetailsService.loadUserByUsername(encryptionService.decrypt(userName))
                : null;
    }

    public HeaderAuthenticationFilter userDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        return this;
    }

    public HeaderAuthenticationFilter headerUtil(HeaderAuthenticationUtil headerAuthenticationUtil) {
        this.headerAuthenticationUtil = headerAuthenticationUtil;
        return this;
    }

    public HeaderAuthenticationFilter encryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
        return this;
    }
}
