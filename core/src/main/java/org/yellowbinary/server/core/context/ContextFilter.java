package org.yellowbinary.server.core.context;

import org.springframework.web.filter.OncePerRequestFilter;
import org.yellowbinary.server.core.service.SessionUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContextFilter extends OncePerRequestFilter {

    private SessionUtil sessionUtil;

    public ContextFilter(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Context.set(httpServletRequest, httpServletResponse);
        sessionUtil.validateAndUpdateTimestamp();
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            Context.clear();
        }
    }

}
