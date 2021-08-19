package com.log.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@SpringBootConfiguration
@SpringBootApplication(scanBasePackages = {
		"com.log.server.biz",
		"com.log.server.data",
		"com.log.server.controller"
})
//@ImportResource({
//	"../spring-security.xml"	
//})
public class Pluto extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Pluto.class);
    }
    public static void main(String[] args) throws Exception {
    	SpringApplication.run(Pluto.class, args);            
    }
}