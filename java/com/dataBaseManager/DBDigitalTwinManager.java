package com.dataBaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.scheduling.annotation.Async;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jsonOperation.JsonReader;
import com.jsonOperation.JsonWriter;

public class DBDigitalTwinManager extends DataBaseFunctions {
	/* This class responsibe for create , read and update digital twin definition */

	JsonWriter writer = new JsonWriter();
	JsonReader reader = new JsonReader();

	// create new robot table
	public CompletableFuture<String> save(JsonNode json) throws SQLException {
		// TODO Auto-generated method stub
		String id = createDefinitionId();
		JsonWriter.updateDefinitionId(json, id);
		PreparedStatement ps;
		boolean flag = true;
		try {
			ps = getPreparedStatement("INSERT INTO digital_twin VALUES(?,?,?,?,?)");
			ps.setString(1, id);
			ps.setString(2, JsonReader.search(json, "digital_twin_runtime_id"));
			ps.setString(3, JsonReader.search(json, "digital_twin_name"));
			ps.setString(4, JsonReader.convertJsonToString(json));
			ps.setString(5, JsonReader.search(json, "event_type"));
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

	// get json file
	public CompletableFuture<JsonNode> getFullFile(String definationId)
			throws SQLException, JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		definationId=definationId.replaceAll("\"", "");
		PreparedStatement ps = getPreparedStatement("SELECT json FROM digital_twin WHERE digital_twin_id = ?");
		ps.setString(1, definationId);
		ResultSet rs = ps.executeQuery();
		String json = "";
		while (rs.next()) {
			json = rs.getString("json");
		}
		return CompletableFuture.completedFuture(JsonReader.getJsonNode(json));
	}

	/* update digital twin name column value on database table */
	public boolean update_digital_twin__name(String name, String definitionId) throws SQLException {
		PreparedStatement ps = getPreparedStatement(
				"update digital_twin set digital_twin_name=? where digital_twin_id=?");
		ps.setString(1, name);
		ps.setString(2, definitionId);
		return ps.execute();
	}

	/* update digital twin run time Id column value on database table */
	public boolean update_digital_twin_Run_Time_ID(String newRunTimeId, String definitionId) throws SQLException {
		PreparedStatement ps = getPreparedStatement(
				"update digital_twin set digital_twin_runtime_id =? where digital_twin_id=?");
		ps.setString(1, newRunTimeId);
		ps.setString(2, definitionId);
		return ps.execute();
	}

	/*
	 * this method used to read some field value in robot definition we recived
	 * array of fields that we will return the value of it
	 */
	@Async
	public CompletableFuture<List<JsonNode>> getFieldsValue(String definationId, ArrayNode fields) throws SQLException,
			JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		List<String> array = new ArrayList<>();
		JsonNode json = null;
		try {
			json = getFullFile(definationId).get();
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

	/* this method used to update digital twin definiton */
	public boolean update(String definitionId, ArrayNode fields)
			throws SQLException, JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		JsonNode json = null;
		boolean flag = true;
		try {
			json = getFullFile(definitionId).get();
			for (JsonNode jsonNode : fields) {
				Iterator<Entry<String, JsonNode>> iterator = jsonNode.fields();
				while (iterator.hasNext()) {
					Entry<String, JsonNode> entry = iterator.next();
					String key = entry.getKey();
					JsonNode value = entry.getValue();
					if (key.equals("digital_twin_runtime_id")) {
						update_digital_twin__name(value.toString(), definitionId);
					} else if (key.equals("digital_twin_name")) {
						update_digital_twin_Run_Time_ID(value.toString(), definitionId);
					}
					JsonWriter.update(json, key, value);
				}
			}
			updateValueInJsonFile(json, definitionId);
		} catch (SQLException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/* this method used to update json file in DB table */
	public void updateValueInJsonFile(JsonNode fullFile, String definitionId) {
		// TODO Auto-generated method stub
		String propertiesString = JsonReader.convertJsonToString(fullFile);
		PreparedStatement ps;
		try {
			ps = getPreparedStatement("update digital_twin set json = ? where digital_twin_id=?");
			ps.setString(1, propertiesString);
			ps.setString(2, definitionId);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/* this method used for create definition Id */
	public String createDefinitionId() {
		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString();
		return uuidAsString;
	}
}
