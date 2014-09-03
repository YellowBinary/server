package org.yellowbinary.server.backend.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CmsConfigurationSupport.class)
public @interface EnableCms {
}
