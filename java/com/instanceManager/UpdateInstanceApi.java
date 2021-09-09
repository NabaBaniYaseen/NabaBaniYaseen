package com.instanceManager;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;

@RestController
public class UpdateInstanceApi {

	/*
	 * This Api contain one method that used to update Instance from any type of
	 * robot or from digitalTwin
	 */
	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
	@PutMapping(path = "/updateInstance/{id}/{name}/{type}")
	private CompletableFuture<String> updateInstance(@PathVariable String id, @PathVariable String name, @PathVariable String type,
			@RequestBody ArrayNode fields) throws JsonProcessingException, SQLException {
		// TODO Auto-generated method stub
		return RobotContainer.updateInstance(id, name, fields, type);
	}
}
