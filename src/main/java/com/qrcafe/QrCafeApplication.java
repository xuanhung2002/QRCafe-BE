package com.qrcafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QrCafeApplication {

	public static void main(String[] args) {

		System.out.println("Test feature 1");
		SpringApplication.run(QrCafeApplication.class, args);
	}

}
