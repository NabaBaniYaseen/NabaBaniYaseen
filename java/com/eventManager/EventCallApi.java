package com.eventManager;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.script.ScriptException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.RequestBody;

import com.sequencer.Sequencer;

@RestController
public class EventCallApi {
	/*
	 * this rest api for call event when the user want to create digital twin
	 * instance with Synchronous and Asynchronous event type
	 */
	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
	@PostMapping(path = "/createDigiatlTwinInstance/{definitionId}/{name}/{type}")
	public CompletableFuture<String[]> createInstance(@PathVariable String definitionId, @PathVariable String name, @PathVariable String type,
			@RequestBody JsonNode json) {
		// TODO Auto-generated method stub
		System.out.println("createDigiatlTwinInstance");
		System.out.println(definitionId);
		System.out.println(name);
		System.out.println(type);

		try {
			 Sequencer.createDigitalTwinInstance(definitionId, name, type, json);
		} catch (JsonProcessingException | InterruptedException | ExecutionException | SQLException
				| ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
