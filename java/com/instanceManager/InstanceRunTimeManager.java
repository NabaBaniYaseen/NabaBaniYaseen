package com.instanceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.dataBaseManager.DBRobotManager;
import com.dataBaseManager.DataBaseFunctions;
import com.entities.DigitalTwin;
import com.entities.IntegratorRobot;
import com.entities.LogicalRobot;
import com.entities.PredictorRobot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jsonOperation.JsonReader;
import com.jsonOperation.JsonWriter;

public class InstanceRunTimeManager extends DataBaseFunctions {
	static JsonReader reader = new JsonReader();
	static JsonWriter writer = new JsonWriter();
	DBRobotManager dBRobotManager = new DBRobotManager();

	// this method update transit variable values on data base
	public static boolean updateVariablesValueOnDB(String runTimeId, ArrayNode jsonArray) throws SQLException {
		// TODO Auto-generated method stub
		String jsonAsString = null;
		PreparedStatement ps = getPreparedStatement("SELECT Json FROM transit_data WHERE run_time_id = ?");
		ps.setString(1, runTimeId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			jsonAsString = rs.getString("Json");
		}
		JsonNode json = JsonWriter.getJsonNode(jsonAsString);
		for (JsonNode jsonNode : jsonArray) {
			Iterator<Entry<String, JsonNode>> iterator = ((JsonNode) jsonNode).fields();
			while (iterator.hasNext()) {
				Entry<String, JsonNode> entry = iterator.next();
				String key = entry.getKey();
				JsonNode value = entry.getValue();
				JsonWriter.update(json, key, value);
			}
		}

		return updateJson(json, runTimeId);
	}

	// this method used to update transit data on DB and in bean instance
	public static boolean updateReturnValue(String runTimeId, String name, String type, ArrayNode fields) {
		try {
			RobotContainer.updateInstance(runTimeId, name, fields, type);
			return updateVariablesValueOnDB(runTimeId, fields);
		} catch (JsonProcessingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	// this method used to update transit data on DB and in bean instance
	public static boolean updateValues(String runTimeId, String name, String type, ArrayNode values,
			String adapterType) {
		try {
			// TODO Auto-generated method stub
			String jsonAsString = null;
			PreparedStatement ps = getPreparedStatement("SELECT Json FROM transit_data WHERE run_time_id = ?");
			ps.setString(1, runTimeId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				jsonAsString = rs.getString("Json");
			}
			JsonNode json = JsonWriter.getJsonNode(jsonAsString);
			writer.updateParameter(json, values, adapterType);
			RobotContainer.updateInstance(runTimeId, name, type, json);
			return updateJson(json, runTimeId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	// this method update attribute values for instance fron IntegratorRopot class
	public static IntegratorRobot updateValuesOnBeanInstance(IntegratorRobot integratorRobot, String runTimeId,
			ArrayNode jsonArray) throws SQLException {
		// TODO Auto-generated method stub
		JsonNode json = integratorRobot.getJson();
		updateValueOnBeanInstance(json, jsonArray);
		integratorRobot.setJson(json);
		return integratorRobot;
	}

	// this method update attribute values for instance fron PredictorRobot class
	public static PredictorRobot updateValuesOnBeanInstance(PredictorRobot predictorRobot, String runTimeId,
			ArrayNode jsonArray) throws SQLException {
		// TODO Auto-generated method stub
		JsonNode json = predictorRobot.getJson();
		updateValueOnBeanInstance(json, jsonArray);
		predictorRobot.setJson(json);
		return predictorRobot;
	}

	// this method update attribute values for instance fron LogicalRobot class
	public static LogicalRobot updateValuesOnBeanInstance(LogicalRobot logicalRobot, String runTimeId,
			ArrayNode jsonArray) throws SQLException {
		// TODO Auto-generated method stub
		JsonNode json = logicalRobot.getJson();
		updateValueOnBeanInstance(json, jsonArray);
		logicalRobot.setJson(json);
		return logicalRobot;
	}

	// this method update attribute values for instance fron DigitalTwin class
	public static DigitalTwin updateValuesOnBeanInstance(DigitalTwin digitalTwin, String runTimeId, ArrayNode jsonArray)
			throws SQLException {
		// TODO Auto-generated method stub
		JsonNode json = digitalTwin.getJson();
		updateValueOnBeanInstance(json, jsonArray);
		digitalTwin.setJson(json);
		return digitalTwin;
	}

	// this method update Json attribute value
	public static void updateValueOnBeanInstance(JsonNode json, ArrayNode jsonArray) {
		// TODO Auto-generated method stub
		for (JsonNode jsonNode : jsonArray) {
			Iterator<Entry<String, JsonNode>> iterator = ((JsonNode) jsonNode).fields();
			while (iterator.hasNext()) {
				Entry<String, JsonNode> entry = iterator.next();
				String key = entry.getKey();
				JsonNode value = entry.getValue();
				JsonWriter.update(json, key, value);
			}
		}
	}

	/* update Json file in transit date table and save it on database */
	private static boolean updateJson(JsonNode json, String runTimeId) throws SQLException {
		String jsonAsString = JsonWriter.convertJsonToString(json);
		PreparedStatement ps = getPreparedStatement("update transit_data set Json =? where run_time_id=?");
		ps.setString(1, jsonAsString);
		ps.setString(2, runTimeId);
		return ps.execute();
	}

	public static JsonNode getInstance(String runTimeId, String name, String type)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		if (type.equals("IntegratorRobot")) {
			return getIntegratorInstance(runTimeId, name);
		} else if (type.equals("PredictorRobot")) {
			return getPredictorInstance(runTimeId, name);
		} else {
			return getLogicFlowInstance(runTimeId, name);
		}
	}

	/*
	 * this method used to get some values that are stored in transit_date table for
	 * instance fron integratorRobot class
	 */
	public static List<JsonNode> getInstance(String runTimeId, String name, String type, ArrayNode fields)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		JsonNode json = getInstance(runTimeId, runTimeId, type);
		return JsonReader.searchForEntity(json, getValue(json, fields));
	}

	public static JsonNode getAdapter(String runTimeId, String name, String adapterName)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		JsonNode json = getInstance(runTimeId, name, "IntegratorRobot");
		return JsonReader.searchForEntityy(json,adapterName);
	}
	public static JsonNode getFunctions(String runTimeId, String name )
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		JsonNode json = getInstance(runTimeId, name, "LogicalRobot");
		return JsonReader.searchForEntityy(json,"Functions");
	}
	
	public static JsonNode getDigitalTwin(String runTimeId, String name )
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		JsonNode json = getInstance(runTimeId, name, "PresentorlRobot");
		return JsonReader.searchForEntityy(json,"digital_twin");
	}
	

	private static JsonNode getIntegratorInstance(String runTimeId, String name)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return RobotContainer.getValuesFromIntegratorInstance(runTimeId, name).get().getJson();
	}

	public static JsonNode getPredictorInstance(String runTimeId, String name)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return RobotContainer.getValuesFromPredictorInstance(runTimeId, name).get().getJson();
	}

	public static JsonNode getLogicFlowInstance(String runTimeId, String name)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return RobotContainer.getValuesFromLogicFlowInstance(runTimeId, name).get().getJson();
	}
	
	public static JsonNode getPresentorInstance(String runTimeId, String name)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return RobotContainer.getValuesFromLogicFlowInstance(runTimeId, name).get().getJson();
	}


	/*
	 * this method used to get some values that are stored in transit_date table for
	 * instance fron integratorRobot class
	 */
	public static List<JsonNode> getValue(IntegratorRobot integratorRobot, ArrayNode fields)
			throws SQLException, JsonMappingException, JsonProcessingException {
		JsonNode json = integratorRobot.getJson();
		return JsonReader.searchForEntity(json, getValue(json, fields));
	}

	/*
	 * this method used to get some values that are stored in transit_date table for
	 * instance fron PredictorRobot class
	 */
	public static List<JsonNode> getValue(PredictorRobot predictorRobot, ArrayNode fields)
			throws SQLException, JsonMappingException, JsonProcessingException {
		JsonNode json = predictorRobot.getJson();
		return JsonReader.searchForEntity(json, getValue(json, fields));
	}

	/*
	 * his method used to get some values that are stored in transit_date table for
	 * instance fron LogicalRobot class
	 */
	public static List<JsonNode> getValue(LogicalRobot logicalRobot, ArrayNode fields)
			throws SQLException, JsonMappingException, JsonProcessingException {
		JsonNode json = logicalRobot.getJson();
		return JsonReader.searchForEntity(json, getValue(json, fields));
	}

	/*
	 * his method used to get some values that are stored in transit_date table for
	 * instance fron DigitalTwin class
	 */
	public static List<JsonNode> getValue(DigitalTwin digitalTwin, ArrayNode fields)
			throws SQLException, JsonMappingException, JsonProcessingException {
		JsonNode json = digitalTwin.getJson();
		return JsonReader.searchForEntity(json, getValue(json, fields));
	}

	/*
	 * this method read array node containing all field the names of the columns to
	 * be read and converts them into an array
	 */
	public static List<String> getValue(JsonNode json, ArrayNode fields) {
		// TODO Auto-generated method stub
		List<String> array = new ArrayList<>();
		for (JsonNode jsonNode : fields) {
			Iterator<Entry<String, JsonNode>> iterator = jsonNode.fields();
			while (iterator.hasNext()) {
				Entry<String, JsonNode> entry = iterator.next();
				String key = entry.getKey();
				array.add(key);
			}
		}
		return array;
	}

	// this method used for reading json
	public static JsonNode getFullJsonFile(String runTimeId) throws SQLException {
		String jsonAsString = null;
		PreparedStatement ps = getPreparedStatement("SELECT Json FROM transit_data WHERE run_time_id = ?");
		ps.setString(1, runTimeId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			jsonAsString = rs.getString("Json");
		}
		return JsonWriter.getJsonNode(jsonAsString);
	}
//
//	// this method used to get sequence of robot in digital twin
//	public static ArrayNode readRobotSequence(String runTimeId, String digitalTwinName) {
//		// TODO Auto-generated method stub
//		try {
//			CompletableFuture<DigitalTwin> digitalTwin = RobotContainer.getDigitalTwinInstance(runTimeId,
//					digitalTwinName);
//			JsonNode json = digitalTwin.get().getJson();
//			return reader.readRobotSequence(json);
//
//		} catch (InterruptedException | ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public static JsonNode getDigitalTwinInstance(String runTimeId, String digitalTwinName) {
		// TODO Auto-generated method stub
		try {
			CompletableFuture<DigitalTwin> digitalTwin = RobotContainer.getDigitalTwinInstance(runTimeId,
					digitalTwinName);
			JsonNode json = digitalTwin.get().getJson();
			return json;
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
