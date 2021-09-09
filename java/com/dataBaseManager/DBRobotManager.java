package com.dataBaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Map.Entry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jsonOperation.JsonReader;
import com.jsonOperation.JsonWriter;

/* This class responsibe for create , read and update all robot type */
@Service
public class DBRobotManager extends DataBaseFunctions implements ComponentManager {
	// create new robot table
	@Async
	@Override
	public CompletableFuture<String> save(JsonNode json, String tableName) {
		String id = createDefinitionId();
		if (tableName.equals("integrator_robots")) {
			return saveIntegrator(json);
		} /*
			 * if the robot type is integrator_robots we not need to store action_ID value
			 * so we save the record using another function. if the type of robot predictor
			 * or logic flow we need to store action_ID value
			 */

		JsonWriter.updateDefinitionId(json, id);
		PreparedStatement ps;
		boolean flag = true;
		try {
			ps = getPreparedStatement("INSERT INTO " + tableName + " VALUES(?,?,?,?,?,?)");
			ps.setString(1, id);
			ps.setString(2, JsonReader.search(json, "robot_run_time_id"));
			ps.setString(3, JsonReader.search(json, "robot_name"));
			ps.setString(4, JsonReader.search(json, "digital_twin_definition_ID"));
			ps.setString(5, JsonReader.search(json, "action_ID"));
			ps.setString(6, JsonReader.convertJsonToString(json));
			ps.execute();
//			System.err.println(Thread.currentThread());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			flag = false;
			e.printStackTrace();
		}
		if (flag) {
			return CompletableFuture.completedFuture(id);
		}
		return CompletableFuture.completedFuture(null);
	}

	// create a record in integrator robot table
	private CompletableFuture<String> saveIntegrator(JsonNode json) {
		// TODO Auto-generated method stub
		String id = createDefinitionId();
		JsonWriter.updateDefinitionId(json, id);
		PreparedStatement ps;
		boolean flag = true;
		try {
			ps = getPreparedStatement("INSERT INTO integrator_robots VALUES(?,?,?,?,?)");
			ps.setString(1, id);
			ps.setString(2, JsonReader.search(json, "robot_run_time_id"));
			ps.setString(3, JsonReader.search(json, "robot_name"));
			ps.setString(4, JsonReader.search(json, "digital_twin_definition_ID"));
			ps.setString(5, JsonReader.convertJsonToString(json));
			ps.execute();
//			System.err.println(Thread.currentThread());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			flag = false;
			e.printStackTrace();
		}
		if (flag == true) {
			return CompletableFuture.completedFuture(id);
		}
		return null;
	}

	/* this method used for create definition Id */
	public String createDefinitionId() {
		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString();
		return uuidAsString;
	}

	// get properties json file
	@Override
	public CompletableFuture<JsonNode> getFullFile(String definitionId, String tableName)
			throws SQLException, JsonMappingException {
		// TODO Auto-generated method stub
		definitionId = definitionId.replace("\"", "");
		PreparedStatement ps = getPreparedStatement(
				"SELECT properties FROM " + tableName + " WHERE robot_definition_id = ?");
		ps.setString(1, definitionId);
		ResultSet rs = ps.executeQuery();
		String json = "";
		while (rs.next()) {
			json = rs.getString("properties");
		}
		return CompletableFuture.completedFuture(JsonReader.getJsonNode(json));
	}

	/* update robot name column value on database table */
	public boolean update_robot_name(String robotName, String definitionId, String tableName) throws SQLException {
		PreparedStatement ps = getPreparedStatement(
				"update" + tableName + " set robot_name=? where robot_definition_id=?");
		ps.setString(1, robotName);
		ps.setString(2, definitionId);
		return ps.execute();
	}

	/* update digital twin definition Id column value on database table */
	public boolean update_digital_twin_definition_ID(String digitalTwinId, String definitionId, String tableName)
			throws SQLException {
		PreparedStatement ps = getPreparedStatement(
				"update " + tableName + " set digitalTwinId =? where robot_definition_id=?");
		ps.setString(1, digitalTwinId);
		ps.setString(2, definitionId);
		return ps.execute();
	}

	/* update event Id column value on database table */
	public boolean update_event_ID(String eventId, String definitionId, String tableName) throws SQLException {
		PreparedStatement ps = getPreparedStatement(
				"update" + tableName + " set event_ID =? where robot_definition_id=?");
		ps.setString(1, eventId);
		ps.setString(2, definitionId);
		return ps.execute();
	}

	/* update action Id column value on database table */
	public boolean update_action_ID(String eventId, String definitionId, String tableName) throws SQLException {
		PreparedStatement ps = getPreparedStatement(
				"update" + tableName + " set event_ID =? where robot_definition_id=?");
		ps.setString(1, eventId);
		ps.setString(2, definitionId);
		return ps.execute();
	}

	/*
	 * this method used to read some field value in robot definition we recived
	 * array of fields that we will return the value of it
	 */
	@Async
	@Override
	public CompletableFuture<List<JsonNode>> getFieldsValue(String definationId, ArrayNode fields, String tableName)
			throws SQLException, JsonMappingException, JsonProcessingException {
		List<String> array = new ArrayList<>();
		JsonNode json = null;
		try {
			json = getFullFile(definationId, tableName).get();
		} catch (JsonMappingException | InterruptedException | ExecutionException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (JsonNode jsonNode : fields) {
			Iterator<Entry<String, JsonNode>> iterator = jsonNode.fields();
			while (iterator.hasNext()) {
				Entry<String, JsonNode> entry = iterator.next();
				String key = entry.getKey();
				array.add(key);
			}
		}
//		System.err.println(Thread.currentThread());
		return CompletableFuture.completedFuture(JsonReader.searchForEntity(json, array));
	}

	/* this method used to read a value of a single entity */
	@Async
	public JsonNode getFieldsValue(String definationId, String field, String tableName)
			throws SQLException, JsonMappingException, JsonProcessingException {
		JsonNode json = null;
		getFullFile(definationId, tableName).complete(json);
//		System.err.println(Thread.currentThread());
		return JsonReader.searchForEntityy(json, field);
	}

	/* this method used to update robot definiton */
	@Override
	public boolean update(String definitionId, ArrayNode fields, String tableName) throws JsonMappingException {
		// TODO Auto-generated method stub
		JsonNode json = null;
		boolean flag = true;
		try {
			json = getFullFile(definitionId, tableName).get();
			for (JsonNode jsonNode : fields) {
				Iterator<Entry<String, JsonNode>> iterator = jsonNode.fields();
				while (iterator.hasNext()) {
					Entry<String, JsonNode> entry = iterator.next();
					String key = entry.getKey();
					JsonNode value = entry.getValue();
					if (key.equals("RobotName")) {
						update_robot_name(value.toString(), definitionId, tableName);
					} else if (key.equals("digital_twin_definition_ID")) {
						update_digital_twin_definition_ID(value.toString(), definitionId, tableName);
					} else if (key.equals("event_ID")) {
						update_event_ID(value.toString(), definitionId, tableName);
					} else if (key.equals("action_ID")) {
						update_action_ID(value.toString(), definitionId, tableName);
					}
					JsonWriter.update(json, key, value);
				}
			}
			updateValueInJsonFile(json, definitionId, tableName);
		} catch (SQLException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/* this method used to update json file in DB table */
	public void updateValueInJsonFile(JsonNode fullFile, String definitionId, String tableName) {
		// TODO Auto-generated method stub
		String propertiesString = JsonReader.convertJsonToString(fullFile);
		PreparedStatement ps;
		try {
			ps = getPreparedStatement("update " + tableName + " set properties= ? where robot_definition_id=?");
			ps.setString(1, propertiesString);
			ps.setString(2, definitionId);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}