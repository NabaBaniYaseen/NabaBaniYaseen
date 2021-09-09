package com.jsonOperation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonOperation {
	// this class contain common method between reader and writer
	public static ObjectMapper objectMapper = new ObjectMapper();

	// this method convert array of json to array of String
	public static List<String> convertArrayNodeToArray(ArrayNode entitysNames) {
		List<String> fields = new ArrayList<>();
		for (JsonNode jsonNode : entitysNames) {
			fields.add(jsonNode.toString());
		}
		return fields;
	}
	public static JsonNode convertStringToJsonNode(String jsonAsString) throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		return objectMapper.readTree(jsonAsString);
	}
	public static String convertJsonToString(JsonNode node) {
		try {
			return objectMapper.writeValueAsString(node);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// this method convert string to json
	public static JsonNode getJsonNode(String Json) {

		try {
			return objectMapper.readTree(Json);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
