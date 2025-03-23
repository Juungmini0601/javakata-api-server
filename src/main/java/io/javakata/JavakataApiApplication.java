package io.javakata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.javakata.common.config.JwtConfig;

@EnableConfigurationProperties(value = JwtConfig.class)
@SpringBootApplication
public class JavakataApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavakataApiApplication.class, args);
	}

}
