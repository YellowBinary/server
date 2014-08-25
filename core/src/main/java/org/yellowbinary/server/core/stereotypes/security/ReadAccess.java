package org.yellowbinary.server.core.stereotypes.security;

import org.springframework.security.access.prepost.PostAuthorize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PostAuthorize("isAuthorized(returnObject,'read')")
public @interface ReadAccess {
}
