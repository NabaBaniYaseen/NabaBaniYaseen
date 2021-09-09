package com.instanceManager;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

@RestController
public class ReadInstanceApi {
	/*
	 * This Api contain one method that used to read Instance from any type of robot
	 * or from digitalTwin
	 */

	/* this method used for read all key values in instance */
	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
	@GetMapping(path = "/readAllValues/{id}/{name}/{type}")
	public CompletableFuture<JsonNode> readAllFile(@PathVariable String id, @PathVariable String name,
			@PathVariable String type) throws JsonProcessingException, SQLException {
				return null;
		// TODO Auto-generated method stub
//		return RobotContainer.getInstance(id, name, type);
	}

	/* this method used for read some key values in instance */
	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
	@GetMapping(path = "/readValues/{id}/{name}/{type}")
	public CompletableFuture<List<JsonNode>> readSomeKeysValue(@PathVariable String id, @PathVariable String name,
			@PathVariable String type, @RequestBody ArrayNode fields) throws JsonProcessingException, SQLException {
		// TODO Auto-generated method stub
		return RobotContainer.getInstance(id, name, fields, type);
	}
}
