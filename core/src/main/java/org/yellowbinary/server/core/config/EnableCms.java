package org.yellowbinary.server.core.config;

import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DelegatingCmsConfiguration.class)
public @interface EnableCms {
}
