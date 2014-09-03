package org.yellowbinary.server.security.interceptors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.yellowbinary.server.backend.Backend;
import org.yellowbinary.server.backend.Node;
import org.yellowbinary.server.backend.security.Security;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.Provides;

import java.util.Map;

@Interceptor
public class BasicAuthProvider {

    @Provides(base = Backend.Base.SECURITY, with = Security.With.AUTHENTICATION_CURRENT_USER)
    public Authentication getCurrentUser(Node node, String with, Map<String, Object> args) throws Exception {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
