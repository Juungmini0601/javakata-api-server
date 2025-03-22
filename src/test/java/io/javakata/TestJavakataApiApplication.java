package io.javakata;

import org.springframework.boot.SpringApplication;

public class TestJavakataApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(JavakataApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
