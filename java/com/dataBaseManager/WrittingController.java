package com.dataBaseManager;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

@RestController
public class WrittingController {
	DBRobotManager dBRobotManager = new DBRobotManager();
	DBDigitalTwinManager dBDigitalTwinManager = new DBDigitalTwinManager();

	/* This method used to create new robot definition */
	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
	@PostMapping(path = "/createRobot/{tableName}")
	public CompletableFuture<String> createRobotDefinition(@PathVariable String tableName,
			@RequestBody JsonNode jsonObject) throws SQLException {
		return dBRobotManager.save(jsonObject, tableName);
	}

	/* This method used to update robot definition */
	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
	@PutMapping(path = "/updateRobotDefinition/{id}/{tableName}")
	public boolean updateIntegratorRobot(@PathVariable String id, @PathVariable String tableName,
			@RequestBody ArrayNode arrayNode) throws SQLException, JsonProcessingException {
		return dBRobotManager.update(id, arrayNode, tableName);
	}

	/* This method used to create new digital twin definition */
	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
	@PostMapping(path = "/createDigitalTwin")
	public CompletableFuture<String> createDigitalTwinDefinition(@RequestBody JsonNode jsonObject) throws SQLException {
		return dBDigitalTwinManager.save(jsonObject);
	}

	/* This method used to update digital twin definition */
	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
	@PutMapping(path = "/updateDigitalTwinDefinition/{id}")
	public boolean updateIntegratorRobot(@PathVariable String id, @RequestBody ArrayNode arrayNode)
			throws SQLException, JsonProcessingException {
		return dBDigitalTwinManager.update(id, arrayNode);
	}

}
