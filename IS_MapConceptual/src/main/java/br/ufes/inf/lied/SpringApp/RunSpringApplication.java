package br.ufes.inf.lied.SpringApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan( basePackages = { "br.ufes.inf.lied.controllers" } )
public class RunSpringApplication extends SpringBootServletInitializer {
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RunSpringApplication.class);
    }

	public static void main( String[] args ) throws Exception {
		SpringApplication.run( RunSpringApplication.class, args );
	}
}
