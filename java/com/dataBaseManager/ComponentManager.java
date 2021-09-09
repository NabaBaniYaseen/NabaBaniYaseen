package com.dataBaseManager;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public interface ComponentManager {
	/* this interface contain method between all robot type and digital twin */
	
	public CompletableFuture<String> save(JsonNode json, String tableName) throws SQLException;

	public CompletableFuture<JsonNode> getFullFile(String definationId, String tableName)
			throws SQLException, JsonMappingException, JsonProcessingException;

	public CompletableFuture<List<JsonNode>> getFieldsValue(String definationId, ArrayNode fields, String tableName)
			throws SQLException, JsonMappingException, JsonProcessingException, InterruptedException,
			ExecutionException;

	public boolean update(String definitionId, ArrayNode fields, String tableName)
			throws SQLException, JsonMappingException, JsonProcessingException;
}
