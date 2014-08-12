package org.yellowbinary.server.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.yellowbinary.server.core.context.ContextFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Set;

@Configuration
public class MyWebAppInitializer implements WebApplicationInitializer, ServletContextInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(MyWebAppInitializer.class);

    @Override
    public void onStartup(ServletContext container) {

        container.addFilter("contextHolder", new ContextFilter()).addMappingForUrlPatterns(null, false, "/*");


/*
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        // Register and map the context holder filter
        container.addFilter("contextHolder", new ContextFilter()).addMappingForUrlPatterns(null, false, "*/
/*");

        // Register and map the dispatcher servlet
        addServiceDispatcherServlet(container, rootContext);
*/
    }

/*
    private void addServiceDispatcherServlet(ServletContext container, AnnotationConfigWebApplicationContext rootContext) {
        final String SERVICES_MAPPING = "/";

        ServletRegistration.Dynamic dispatcher = container.addServlet("customDefaultServlet", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        Set<String> mappingConflicts = dispatcher.addMapping(SERVICES_MAPPING);

        if (!mappingConflicts.isEmpty()) {
            for (String s : mappingConflicts) {
                LOG.error("Mapping conflict: " + s);
            }
            throw new IllegalStateException("'ServicesDispatcher' could not be mapped to '" + SERVICES_MAPPING + "'");
        }
    }
*/

}