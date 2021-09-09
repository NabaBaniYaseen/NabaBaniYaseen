package com.jsonOperation;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/*
 * this class contain the method that responsible for read any thing in jsons
 * file
 */
public class JsonReader extends JsonOperation {

	/*
	 * This method is responsible for reading the values ​​of a single key in json
	 * file.
	 */
	public static String search(JsonNode node, String entityName) {
		JsonNode jsonNode = searchForEntityy(node, entityName);
		return convertJsonToString(jsonNode);
	}

	public static String searchOnJson(JsonNode node, String entityName) {
		if (node.has(entityName)) {
			return node.get(entityName).toString();
		}
		// not found fall through
		return null;
	}

	/*
	 * This method is responsible for reading the values ​​of a array of keys in
	 * json file.
	 */
	public static List<JsonNode> searchForEntity(JsonNode json, List<String> entitysName) {
		List<JsonNode> values = new ArrayList<JsonNode>();
		try {
			for (int i = 0; i < entitysName.size(); i++) {
				values.add(searchForEntityy(json, entitysName.get(i)));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return values;
	}

	// search for key in json
	public static JsonNode searchForEntityy(JsonNode node, String entityName) {
		// A naive depth-first search implementation using recursion.
		// This will be inefficient (stack overflow) for finding deeply-nested
		// needles or needles toward the end of large object graphs.
		if (node == null) {
			return null;
		}
		if (node.has(entityName)) {
			return node.get(entityName);
		}
		if (node.toString().equals(entityName)) {
			return node.get(entityName);
		}
		for (JsonNode child : node) {
			if (child.isContainerNode()) {
				JsonNode childResult = searchForEntityy(child, entityName);
				if (childResult != null && !childResult.isMissingNode()) {
					return childResult;
				}
			}
		}
		// not found fall through
		return null;
	}
	
	// search for key in json
		public static JsonNode searchForNextDigitalTwin(JsonNode node) {
			// A naive depth-first search implementation using recursion.
			// This will be inefficient (stack overflow) for finding deeply-nested
			// needles or needles toward the end of large object graphs.
			if (node == null) {
				return null;
			}
			if (node.has("Next_Digital_Twin")) {
				return node.get("Next_Digital_Twin");
			}
			if (node.toString().equals("Next_Digital_Twin")) {
				return node.get("Next_Digital_Twin");
			}
			for (JsonNode child : node) {
				if (child.isContainerNode()) {
					JsonNode childResult = searchForNextDigitalTwin(child);
					if (childResult != null && !childResult.isMissingNode()) {
						return childResult;
					}
				}
			}
			// not found fall through
			return null;
		}

	// get robot sequence in digital twin as array
	public static ArrayNode readRobotSequence(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return (ArrayNode) searchForEntityy(jsonNode, "Robots");
	}

	// this method used to get name && type && definitionId for robot
	public static String getRobot(JsonNode node, String entityName) {
		if (node.has(entityName)) {
			return convertJsonToString(node.get(entityName));
		}
		return null;
	}

}
