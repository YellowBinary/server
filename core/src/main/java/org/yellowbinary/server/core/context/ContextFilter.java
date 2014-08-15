package org.yellowbinary.server.core.context;

import org.springframework.web.filter.OncePerRequestFilter;
import org.yellowbinary.server.core.service.SessionService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContextFilter extends OncePerRequestFilter {

    private SessionService sessionService;

    public ContextFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Context.set(httpServletRequest, httpServletResponse);
        sessionService.validateAndUpdateTimestamp();
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            Context.clear();
        }
    }

}
