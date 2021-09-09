package com.eventManager;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootApplication
public class EventManagerMain {
	public static void main(String[] args) {
		SpringApplication.run(EventManagerMain.class, args);
		try {
			EventManager.excuteTimeEvent();
		} catch (JsonProcessingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
