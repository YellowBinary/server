package org.yellowbinary.server.backend.security.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.yellowbinary.server.backend.security.HeaderAuthenticationUtil;
import org.yellowbinary.server.backend.security.Security;
import org.yellowbinary.server.core.service.EncryptionUtil;
import org.yellowbinary.server.core.service.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private HeaderAuthenticationUtil headerAuthenticationUtil;

    private EncryptionUtil encryptionUtil;

    private SessionUtil sessionUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        String token  = headerAuthenticationUtil.setUserName(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
        String timestamp = String.valueOf(sessionUtil.getTimestamp().getMillis());
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode().
                put(Security.Header.TOKEN_KEY, encryptionUtil.encrypt(token)).
                put(Security.Header.SESSION_KEY, encryptionUtil.encrypt(timestamp));
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

    public RestAuthenticationSuccessHandler encryptionService(EncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
        return this;
    }

    public RestAuthenticationSuccessHandler sessionService(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
        return this;
    }

}
