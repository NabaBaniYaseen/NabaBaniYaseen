package com.instanceManager;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sequencer.Sequencer;

@RestController
public class DeleteInstanceApi {
	/*
	 * This Api contain one method that used to delete Instance from any type of
	 * robot or from digitalTwin
	 */
//	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
//	@DeleteMapping(path = "/deleteInstance/{id}/{name}/{type}")
//	public CompletableFuture<String> deleteInstance(@PathVariable String id, @PathVariable String name,
//			@PathVariable String type) {
//		// TODO Auto-generated method stub
//		return Sequencer.deleteInstance(id, name, type);
//	}
//
//	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
//	@DeleteMapping(path = "/deleteDigitalTwin/{id}/{name}/{type}")
//	public CompletableFuture<String> deleteDigitalTwin(@PathVariable String id, @PathVariable String name,
//			@PathVariable String type)
//			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
//		// TODO Auto-generated method stub
//		return Sequencer.deleteDigitalTwinInstance(id, name, type);
//	}
}
