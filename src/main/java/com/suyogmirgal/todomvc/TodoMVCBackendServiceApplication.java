package com.suyogmirgal.todomvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main class for TodoMVCBackendService.
 *
 * @author suyogmirgal
 * created on 2024/04/29
 */
@SpringBootApplication
@EnableJpaAuditing
public class TodoMVCBackendServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoMVCBackendServiceApplication.class, args);
	}

}
