package org.yellowbinary.cms.server.core.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DelegatingCmsConfiguration.class)
public @interface EnableCms {
}
