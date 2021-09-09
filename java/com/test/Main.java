package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dataBaseManager.DataBaseManagerMain;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(DataBaseManagerMain.class, args);

	}
}
