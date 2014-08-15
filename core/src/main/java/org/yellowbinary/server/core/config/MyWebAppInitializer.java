package org.yellowbinary.server.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.yellowbinary.server.core.context.ContextFilter;
import org.yellowbinary.server.core.service.SessionService;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

@Configuration
public class MyWebAppInitializer implements WebApplicationInitializer, ServletContextInitializer {

    @Autowired
    private SessionService sessionService;

    @Override
    public void onStartup(ServletContext container) {

        // Register Context Filter
        addContextHolderFilter(container);

        // Register Encoding Filter
        addEncodingFilter(container);

        // Register Logging Filter
        addLoggingFilter(container);

    }

    private void addContextHolderFilter(ServletContext container) {
        container.addFilter("contextFilter", new ContextFilter(sessionService)).addMappingForUrlPatterns(null, false, "/*");
    }

    private void addEncodingFilter(ServletContext container) {
        FilterRegistration.Dynamic fr = container.addFilter("encodingFilter", new CharacterEncodingFilter());
        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");
    }

    private void addLoggingFilter(ServletContext container) {
        container.addFilter("loggingFilter", new CommonsRequestLoggingFilter()).addMappingForUrlPatterns(null, true, "/*");
    }

}