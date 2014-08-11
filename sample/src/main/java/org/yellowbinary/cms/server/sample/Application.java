package org.yellowbinary.cms.server.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.yellowbinary.cms.server.core.config.MyWebAppInitializer;

@ComponentScan(basePackages = "org.yellowbinary")
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(new Class[] {Application.class, MyWebAppInitializer.class}, args);
    }
}