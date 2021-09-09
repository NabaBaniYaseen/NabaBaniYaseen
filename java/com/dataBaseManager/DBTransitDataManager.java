package com.dataBaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jsonOperation.JsonReader;
import com.jsonOperation.JsonWriter;

public class DBTransitDataManager extends DataBaseFunctions {
	JsonReader reader = new JsonReader();
	JsonWriter writer = new JsonWriter();
	DBRobotManager dbRobotManager = new DBRobotManager();

	// this method update transit variable values
	public boolean updateVariablesValue(String runTimeId, ArrayNode jsonArray) throws SQLException {
		// TODO Auto-generated method stub
		String jsonAsString = null;
		PreparedStatement ps = getPreparedStatement("SELECT Json FROM transit_data WHERE run_time_id = ?");
		ps.setString(1, runTimeId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			jsonAsString = rs.getString("Json");
		}
		JsonNode json = writer.getJsonNode(jsonAsString);
		for (JsonNode jsonNode : jsonArray) {
			Iterator<Entry<String, JsonNode>> iterator = ((JsonNode) jsonNode).fields();
			while (iterator.hasNext()) {
				Entry<String, JsonNode> entry = iterator.next();
				String key = entry.getKey();
				JsonNode value = entry.getValue();
				writer.update(json, key, value);
			}
		}

		return updateJson(json, runTimeId);
	}

	/* update Json file in transit date */
	public boolean updateJson(JsonNode json, String runTimeId) throws SQLException {
		String jsonAsString = writer.convertJsonToString(json);
		PreparedStatement ps = getPreparedStatement("update transit_data set Json =? where run_time_id=?");
		ps.setString(1, jsonAsString);
		ps.setString(2, runTimeId);
		return ps.execute();
	}

	/* This method used to read some values in robot Instance */
	public List<JsonNode> getValue(String runTimeId, ArrayNode fields)
			throws SQLException, JsonMappingException, JsonProcessingException {
		List<String> array = new ArrayList<>();
		JsonNode json = getFullFile(runTimeId);
		for (JsonNode jsonNode : fields) {
			Iterator<Entry<String, JsonNode>> iterator = jsonNode.fields();
			while (iterator.hasNext()) {
				Entry<String, JsonNode> entry = iterator.next();
				String key = entry.getKey();
				array.add(key);
			}
		}
		return reader.searchForEntity(json, array);
	}

	/* This method used to read robot Instance */
	public JsonNode getFullFile(String runTimeId) throws SQLException, JsonMappingException {
		// TODO Auto-generated method stub
		PreparedStatement ps = getPreparedStatement("SELECT Json FROM transit_data WHERE run_time_id = ?");
		ps.setString(1, runTimeId);
		ResultSet rs = ps.executeQuery();
		String json = "";
		while (rs.next()) {
			json = rs.getString("Json");
		}
		return reader.getJsonNode(json);
	}

}
