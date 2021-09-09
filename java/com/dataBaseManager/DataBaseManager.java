package com.dataBaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class DataBaseManager implements ComponentManager {
	/* this class are implemention for Composite pattern*/ 
	// Collection of childs for ComponentManager .
	private final List<ComponentManager> childs = new ArrayList<>();

	// Adds the all instances to the composition.
	public void add(ComponentManager component) {
		childs.add(component);
	}

	// This is only shallowCopy
	public void add(List<ComponentManager> component) {
		childs.addAll(component);
	}

	@Override
	public CompletableFuture<String> save(JsonNode json, String tableName) {
		for (ComponentManager component : childs) {
			try {
				return component.save(json, tableName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Delegation
		}
		return null;
	}
	
	@Override
	public CompletableFuture<JsonNode> getFullFile(String definationId, String tableName)
			throws JsonMappingException, JsonProcessingException {
		for (ComponentManager component : childs) {
			try {
				return component.getFullFile(definationId, tableName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public CompletableFuture<List<JsonNode>> getFieldsValue(String definationId, ArrayNode fields, String tableName)
			throws JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException {
		for (ComponentManager component : childs) {
			try {
				return component.getFieldsValue(definationId, fields, tableName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean update(String definitionId, ArrayNode fields, String tableName)
			throws SQLException, JsonMappingException, JsonProcessingException {
		for (ComponentManager component : childs) {
			return component.update(definitionId, fields, tableName); // Delegation
		}
		return false;
	}

}
