package org.yellowbinary.server.basic_security.interceptors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.yellowbinary.server.core.Core;
import org.yellowbinary.server.core.Node;
import org.yellowbinary.server.core.security.Security;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.Provides;

import java.util.Map;

@Interceptor
public class BasicAuthProvider {

    @Provides(base = Core.Base.SECURITY, with = Security.With.AUTHENTICATION_CURRENT_USER)
    public Authentication getCurrentUser(Node node, String with, Map<String, Object> args) throws Exception {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
