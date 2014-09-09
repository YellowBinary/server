package org.yellowbinary.server.backend.security.rest;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final String AUTHORIZATION_FAILED_JSON = "{\"message\":\"The username or password was not correct.\", \"authorization-failed\":true,\"cause\":\"AUTHORIZATION_FAILURE\"}";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        out.print(AUTHORIZATION_FAILED_JSON);
        out.flush();
        out.close();

    }
}
