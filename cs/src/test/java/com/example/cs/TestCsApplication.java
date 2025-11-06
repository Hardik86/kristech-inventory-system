package com.example.cs;

import org.springframework.boot.SpringApplication;

public class TestCsApplication {

	public static void main(String[] args) {
		SpringApplication.from(CsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
