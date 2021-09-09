package com.dataBaseManager;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

@RestController
public class ReadingController {
	/* this class used to read definition of robots and digital twin */
	DBRobotManager dbRobotManager = new DBRobotManager();
	DBDigitalTwinManager dBDigitalTwinManager = new DBDigitalTwinManager();

	/* this method used to read definition of robot */
	@GetMapping(path = "/getFullFile/{id}/{tableName}")
	public CompletableFuture<JsonNode> readRobotById(@PathVariable String id, @PathVariable String tableName)
			throws JsonMappingException, JsonProcessingException {
		CompletableFuture<JsonNode> jsonObject = null;
		try {
			jsonObject = dbRobotManager.getFullFile(id, tableName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	/* this method used to read some key value in definition of robot */
	@GetMapping(path = "/readValues/{id}/{tableName}")
	public CompletableFuture<List<JsonNode>> readPartOfRobotFieldsValue(@PathVariable String id,
			@RequestBody ArrayNode arrayNode, @PathVariable String tableName)
			throws SQLException, JsonMappingException, JsonProcessingException {
		return dbRobotManager.getFieldsValue(id, arrayNode, tableName);
	}

	/* this method used to read definition of digital twin */
	@GetMapping(path = "/getFullFile/{id}")
	public CompletableFuture<JsonNode> readDigitalTwin(@PathVariable String id)
			throws JsonMappingException, JsonProcessingException {
		CompletableFuture<JsonNode> jsonObject = null;
		try {
			jsonObject = dBDigitalTwinManager.getFullFile(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	/* this method used to read some key value in definition of digital twin */
	@GetMapping(path = "/readValues/{id}")
	public CompletableFuture<List<JsonNode>> readPartOfDigitalTwin(@PathVariable String id,
			@RequestBody ArrayNode arrayNode) throws SQLException, JsonMappingException, JsonProcessingException {
		try {
			return dBDigitalTwinManager.getFieldsValue(id, arrayNode);
		} catch (JsonProcessingException | SQLException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
