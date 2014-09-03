package org.yellowbinary.server.backend.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yellowbinary.server.core.InterceptorRepository;

@Component
public class SecurityEventGenerator {

    @Autowired
    private InterceptorRepository interceptorRepository;

}
