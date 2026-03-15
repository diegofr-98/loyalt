package com.loyalt.loyalt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
public class LoyaltApplication {


	public static void main(String[] args) {
		SpringApplication.run(LoyaltApplication.class, args);
	}

}
