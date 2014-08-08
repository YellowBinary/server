package org.yellowbinary.cms.server.core.config;

import org.yellowbinary.cms.server.core.annotation.AnnotationProcessor;
import org.springframework.context.annotation.Bean;

public class CmsCoreConfigurationSupport {

    @Bean
    public AnnotationProcessor createCoreBeanPostProcessor() {
        return new AnnotationProcessor();
    }

}
