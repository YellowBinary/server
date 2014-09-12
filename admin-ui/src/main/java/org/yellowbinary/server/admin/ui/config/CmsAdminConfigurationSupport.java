package org.yellowbinary.server.admin.ui.config;

import com.google.common.collect.Sets;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.Set;

@EnableWebMvc
@ComponentScan(basePackages = {"org.yellowbinary.server.admin.ui"})
@Configuration
public class CmsAdminConfigurationSupport {

/*
    @Bean(name = "viewResolver")
    public ViewResolver getInternalResourceViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix(String.format("%s/views/", prefix));
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        resolver.setExposePathVariables(true);
        return resolver;
    }
*/

    public ServletContextTemplateResolver servletContextTemplateResolver() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setPrefix("views/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(1);
        return resolver;
    }

    public ClassLoaderTemplateResolver classLoaderTemplateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("views/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(2);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        Set<TemplateResolver> templateResolvers = Sets.newHashSet();
        templateResolvers.add(servletContextTemplateResolver());
        templateResolvers.add(classLoaderTemplateResolver());
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolvers(templateResolvers);
        return engine;
    }

    @Bean(name = "defaultTemplateResolver")
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        return resolver;
    }

/*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry aRegistry) {
        ResourceHandlerRegistration resourceHandlerRegistration = aRegistry.addResourceHandler("/views** /*");
        resourceHandlerRegistration.addResourceLocations("classpath:/views/");
    }
*/

    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.setName("adminDispatcherServlet");
        registration.addUrlMappings("/admin/*");
        return registration;
    }

}
