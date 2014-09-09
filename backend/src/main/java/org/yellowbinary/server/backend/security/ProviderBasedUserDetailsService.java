package org.yellowbinary.server.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.backend.Backend;
import org.yellowbinary.server.core.InterceptorException;
import org.yellowbinary.server.core.ModuleException;
import org.yellowbinary.server.core.event.ProvidesEventGenerator;

import java.util.Collections;

@Service
public class ProviderBasedUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(ProvidesEventGenerator.class);

    @Autowired
    private ProvidesEventGenerator providesEventGenerator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            UserDetails userDetails = providesEventGenerator.triggerInterceptor(null, Backend.Base.SECURITY,
                    Security.With.AUTHORIZATION_SUBJECT, Collections.<String, Object>singletonMap("username", username));
            if (userDetails != null) {
                return userDetails;
            }
        } catch (ModuleException e) {
            LOG.error(String.format("Module [%s] failed, cause: %s", e.getName(), e.getExceptionCause().name()), e);
        } catch (InterceptorException e) {
            LOG.error(String.format("Invocation of module [%s] failed", e.getModule()), e);
        }

        throw new UsernameNotFoundException(String.format("User '%s' not found", username));
    }

}
