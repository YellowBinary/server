package org.yellowbinary.server.security.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.yellowbinary.server.security.service.HeaderAuthenticationUtil;
import org.yellowbinary.server.backend.security.Security;
import org.yellowbinary.server.core.service.EncryptionService;
import org.yellowbinary.server.core.service.SessionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private HeaderAuthenticationUtil headerAuthenticationUtil;

    private EncryptionService encryptionService;

    private SessionService sessionService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        String token  = headerAuthenticationUtil.setUserName(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
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

    public RestAuthenticationSuccessHandler headerUtil(HeaderAuthenticationUtil headerAuthenticationUtil) {
        this.headerAuthenticationUtil = headerAuthenticationUtil;
        return this;
    }

    public RestAuthenticationSuccessHandler encryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
        return this;
    }

    public RestAuthenticationSuccessHandler sessionService(SessionService sessionService) {
        this.sessionService = sessionService;
        return this;
    }

}
