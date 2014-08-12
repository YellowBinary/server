package org.yellowbinary.server.core.config;

import org.yellowbinary.server.core.annotation.AnnotationProcessor;
import org.springframework.context.annotation.Bean;

public class CmsCoreConfigurationSupport {

    @Bean
    public AnnotationProcessor createCoreBeanPostProcessor() {
        return new AnnotationProcessor();
    }

}
