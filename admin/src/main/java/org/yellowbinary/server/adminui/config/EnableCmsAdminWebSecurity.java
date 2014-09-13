package org.yellowbinary.server.adminui.config;

import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.yellowbinary.server.backend.config.security.CmsWebSecurityConfiguration;
import org.yellowbinary.server.backend.config.security.DefaultSecurityConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableWebMvcSecurity
@Import({DefaultSecurityConfiguration.class, CmsWebSecurityConfiguration.class})
public @interface EnableCmsAdminWebSecurity {
}
