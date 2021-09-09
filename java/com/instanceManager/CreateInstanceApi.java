package com.instanceManager;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sequencer.Sequencer;

@RestController
public class CreateInstanceApi {

//	/*
//	 * This Api contain one method that used to create Instance from any type of
//	 * robot or from digitalTwin
//	 */
//	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
//	@PostMapping(path = "/createInstance/{id}/{type}/{name}")
//	private CompletableFuture<String> createInstance(@PathVariable String id, @PathVariable String type,
//			@PathVariable String name)
//			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
//		// TODO Auto-generated method stub
//		return Sequencer.createRobotInstance(type, id, name);
//	}
//	
//	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
//	@PostMapping(path = "/createDigitalTwin/{id}/{type}/{name}")
//	private CompletableFuture<String> createDigitalTwin(@PathVariable String id, @PathVariable String type,
//			@PathVariable String name)
//			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
//		// TODO Auto-generated method stub
//		return Sequencer.createRobotInstance(type, id, name);
//	}
}
