package io.javakata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// TODO 여기 길어지면 제거 예정
@EnableJpaAuditing
@SpringBootApplication
public class JavakataApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavakataApiApplication.class, args);
	}

}
