package com.log.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PlutoWebMvcConfiguration implements WebMvcConfigurer{

	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/application.htm");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
	
}
