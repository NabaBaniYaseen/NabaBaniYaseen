package com.runTimeController;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.dataBaseManager.DataBaseFunctions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.instanceManager.InstanceRunTimeManager;
import com.jsonOperation.JsonOperation;
import com.jsonOperation.JsonReader;
import com.jsonOperation.JsonWriter;
import com.mysql.cj.x.protobuf.MysqlxCrud.Update;

public class HelperFunction extends DataBaseFunctions {
	/* this class call methods in another package */
	public static JsonNode getPresentor(String runTimeId, String name){
		try {
			return InstanceRunTimeManager.getPresentorInstance(runTimeId, name);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	// get adpter array node
	public static ArrayNode getAdapter(String runTimeId, String robotName, String adapterName) {
		// TODO Auto-generated method stub
		try {
			return (ArrayNode) InstanceRunTimeManager.getAdapter(runTimeId, robotName, adapterName);
		} catch (JsonProcessingException | SQLException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String getAction(JsonNode json) {
		// TODO Auto-generated method
		return JsonReader.convertJsonToString(JsonReader.searchForEntityy(json, "action_type"));
	}

	public static String getRobot(JsonNode robot, String entity) {
		// TODO Auto-generated method stub
		return JsonReader.getRobot(robot, entity);
	}

	// get value of all parameter in adapters
	public static String searchForParameter(JsonNode parameters, String parameterName) {
		// TODO Auto-generated method stub
		return JsonReader.convertJsonToString(JsonReader.searchForEntityy(parameters, parameterName));
	}

	// used to search for specfic type function
	public static JsonNode searchForFunction(JsonNode jsonNode, String functionType) {
		// TODO Auto-generated method stub
		return JsonReader.searchForEntityy(jsonNode, functionType);
	}

	public static void Update(JsonNode json, String key, String value) {
		JsonWriter.update(json, key, value);
	}

	// used to search for specfic type function
	public static JsonNode searchForKeyValue(JsonNode jsonNode, String entity) {
		// TODO Auto-generated method stub
		return JsonReader.searchForEntityy(jsonNode, entity);
	}

	// used to search for next digital Twin
	public static JsonNode searchForKeyValue(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return JsonReader.searchForNextDigitalTwin(jsonNode);
	}

	public static ArrayNode readRopotSequence(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return JsonReader.readRobotSequence(jsonNode);
	}

	// used to search for specfic type function
	public static String searchForString(JsonNode jsonNode, String entity) {
		// TODO Auto-generated method stub
		return JsonReader.convertJsonToString(JsonReader.searchForEntityy(jsonNode, entity)).replaceAll("\"", "");
	}

	// used to search for operation value
	public static String getOperationValue(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return JsonReader.convertJsonToString(JsonReader.searchForEntityy(jsonNode, "operation_value"));
	}

	// get Functions input
	public static ArrayNode getFunctionsInput(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return (ArrayNode) JsonReader.searchForEntityy(jsonNode, "input");
	}

	// get Functions output
	public static ArrayNode getFunctionsOutput(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return (ArrayNode) JsonReader.searchForEntityy(jsonNode, "output");
	}

	// get Functions output
	public static JsonNode getFunctionsOutputJson(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return JsonReader.searchForEntityy(jsonNode, "output");
	}

	// get values from json node as json array
	public static ArrayNode getArrayNode(JsonNode jsonNode, String arrayName) {
		// TODO Auto-generated method stub
		return (ArrayNode) JsonReader.searchForEntityy(jsonNode, arrayName);
	}

	// get Functions type
	public static String getOperationType(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return JsonReader.convertJsonToString(JsonReader.searchForEntityy(jsonNode, "operation_type")).replaceAll("\"",
				"");
	}

	public static String getUserDefienedFunctionType(JsonNode jsonNode) {
		// TODO Auto-generated method stub""
		return JsonReader.convertJsonToString(JsonReader.searchForEntityy(jsonNode, "user_defined_algorithm_type"))
				.replaceAll("\"", "");
	}

	public static String getUserDefienedFunctionValue(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return JsonReader.convertJsonToString(JsonReader.searchForEntityy(jsonNode, "user_defined_algorithm_value"))
				.replaceAll("\"", "");

	}

	// this method used to update data base adapter return value in json file
	public static boolean updateAdapterReturnValue(String runTimeId, String robotName,
			ArrayNode returnValueForSqlStatment) {
		// TODO Auto-generated method stub
		return InstanceRunTimeManager.updateReturnValue(runTimeId, robotName, "IntegratorRobot",
				returnValueForSqlStatment);
	}

	// this method used to update adapter return value in json file
	public static boolean updateAdapterReturnValue(String runTimeId, String name, ArrayNode values,
			String adapterName) {
		// TODO Auto-generated method stub
		return InstanceRunTimeManager.updateValues(runTimeId, name, "IntegratorRobot", values, adapterName);
	}

	// create array node
	public static ArrayNode createArrayNode() {
		// TODO Auto-generated method stub
		return JsonOperation.objectMapper.createArrayNode();
	}

	// get adpter array node
	public static ArrayNode getFunctions(String runTimeId, String robotName) {
		// TODO Auto-generated method stub
		return getFunction(runTimeId, robotName);
	}

	public static ArrayNode getFunction(String runTimeId, String robotName) {
		// TODO Auto-generated method stub
		try {
			return (ArrayNode) InstanceRunTimeManager.getFunctions(runTimeId, robotName);
		} catch (JsonProcessingException | SQLException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayNode getDigitalTwinFromPresentor(String runTimeId, String robotName) {
		// TODO Auto-generated method stub
		try {
			return (ArrayNode) InstanceRunTimeManager.getDigitalTwin(runTimeId, robotName);
		} catch (JsonProcessingException | SQLException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static List<String> convertArrayNodeToArray(ArrayNode inputs) {
		return JsonOperation.convertArrayNodeToArray(inputs);

	}

	public static String search(JsonNode node, String label) {
		if (node.get("label").toString().equals(label)) {
			return node.get("value").toString();
		}
		return null;
	}

	public static JsonNode searchOnJson(JsonNode node, String label) {
		if (node.get("label").toString().equals(label)) {
			return node.get("value");
		}
		return null;
	}

	public static String getLabel(JsonNode node) {
		return node.get("label").toString();
	}

	public static String searchForValues(JsonNode jsonNode, String entityName) {
		// TODO Auto-generated method stub
		return JsonReader.convertJsonToString(JsonReader.searchForEntityy(jsonNode, entityName));
	}

	public static String convertJsonNodeToString(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return JsonReader.convertJsonToString(jsonNode);
	}

	public static JsonNode convertStringToJsonNode(String string) throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		return JsonOperation.convertStringToJsonNode(string);
	}

	// this method used to create json node
	public static JsonNode createJsonNode() throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		return JsonOperation.objectMapper.createObjectNode();
	}

}
