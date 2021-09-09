package com.jsonOperation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonWriter extends JsonOperation {
	// this class contain the method that responsible for writing in json file

	public static void updateDefinitionId(JsonNode json, String definitionId) {
		update(json, "definition_id", definitionId);
	}

	public void updateRunTimeId(JsonNode json, String runTime) {
		update(json, "Robot_Run_Time_ID", runTime);
	}

	public void updateRobotName(JsonNode json, String name) {
		// TODO Auto-generated method stub
		update(json, "RobotName", name);
	}

	public void updateDigitalTwinName(JsonNode json, String name) {
		// TODO Auto-generated method stub
		update(json, "digital_twin_name", name);
	}

	public void updateDigitalTwinRunTimeId(JsonNode json, String runTimeId) {
		// TODO Auto-generated method stub
		update(json, "digital_twin_runtime_id", runTimeId);
	}

	// for update String value of single entity in json
	public static void update(JsonNode json, String entityName, String value) {
		if (json == null) {
			return;
		}
		if (json.has(entityName)) {
			((ObjectNode) json).put(entityName, value);
			return;
		}
		for (JsonNode child : json) {
			if (child.isContainerNode()) {
				update(child, entityName, value);

			}
		}
		return;
	}

	@SuppressWarnings("deprecation")
	public void updateParameter(JsonNode node, ArrayNode values, String entityName) {
		// A naive depth-first search implementation using recursion.
		// This will be inefficient (stack overflow) for finding deeply-nested
		// needles or needles toward the end of large object graphs.
		if (node == null) {
			return;
		}
		if (node.has(entityName)) {
			int index = 0;
			for (JsonNode Value : values) {
				((ObjectNode) node.get(entityName).get("Parameters").get(index)).put("Value",Value);
				index++;
			}
			return;
		}
		for (JsonNode child : node) {
			if (child.isContainerNode()) {
				updateParameter(child, values, entityName);
			}
		}
		// not found fall through
		return;
	}

	// for update Json value of single entity in json
	@SuppressWarnings("deprecation")
	public static void update(JsonNode node, String entityName, JsonNode value) {
		// A naive depth-first search implementation using recursion.
		// This will be inefficient (stack overflow) for finding deeply-nested
		// needles or needles toward the end of large object graphs.
		if (node == null) {
			return;
		}
		if (node.has(entityName)) {
			((ObjectNode) node).put(entityName, value);
			return;
		}
		for (JsonNode child : node) {
			if (child.isContainerNode()) {
				update(child, entityName, value);
			}
		}
		// not found fall through
		return;
	}
}