package org.yellowbinary.server.basic_auth.interceptors;

import org.yellowbinary.server.core.Core;
import org.yellowbinary.server.core.Node;
import org.yellowbinary.server.core.security.Security;
import org.yellowbinary.server.core.security.User;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.Provides;

import java.util.Map;

@Interceptor
public class BasicAuthProvider {

    @Provides(base = Core.Base.SECURITY, with = Security.With.AUTHENTICATION_CURRENT_USER)
    public User getCurrentUser(Node node, String with, Map<String, Object> args) throws Exception {

        return new User() {
            @Override
            public String getIdentifier() {
                return null;
            }

            @Override
            public String name() {
                return null;
            }

            @Override
            public String type() {
                return null;
            }
        };

    }

}
