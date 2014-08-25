package org.yellowbinary.server.basic_auth.config;

import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableWebMvcSecurity
@Import(BasicSecurityConfiguration.class)
public @interface EnableBasicSecurity {
}