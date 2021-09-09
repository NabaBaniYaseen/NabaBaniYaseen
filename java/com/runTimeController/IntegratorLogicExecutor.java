package com.runTimeController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class IntegratorLogicExecutor {
	static BufferedReader reader;
	static String line;
	static StringBuffer responseContent = new StringBuffer();
	static StringBuffer openSourceEquationBuffer = new StringBuffer();

	// read database adapter parameter and connect to it
	public static boolean readInterfaceParamDB(String runTimeId, String name)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		boolean flag = true;
		ArrayNode DB_Param = HelperFunction.getAdapter(runTimeId, name, "Interface_param_DB");
		for (JsonNode jsonNode : DB_Param) {
			/* get values of DataBase parameter */
			String parameterName = HelperFunction.searchForParameter(jsonNode, "name");
			String pluginId = HelperFunction.searchForParameter(jsonNode, "pluginId");
			String accessType = HelperFunction.searchForParameter(jsonNode, "accessType");
			String hostName = HelperFunction.searchForParameter(jsonNode, "hostName");
			String DBName = HelperFunction.searchForParameter(jsonNode, "DBName");
			String portNumber = HelperFunction.searchForParameter(jsonNode, "portNumber");
			String username = HelperFunction.searchForParameter(jsonNode, "username");
			String password = HelperFunction.searchForParameter(jsonNode, "password");
			String SQL = HelperFunction.searchForParameter(jsonNode, "SQL");
			String params = "Set_Interface_Param_DB /" + parameterName + "/" + pluginId + "/" + accessType + "/"
					+ hostName + "/" + DBName + "/" + portNumber + "/" + username + "/" + password + "/" + SQL;
			params = params.replace("\"", "");
			ArrayNode returnValues = sendRequest(params);
			// update return value for adapter value
			flag &= HelperFunction.updateAdapterReturnValue(runTimeId, name, returnValues);
		}
		return flag;
	}

	// read json adapter parameter and connect to it
	public static void readInterfaceParamJSON(String runTimeId, String name)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		ArrayNode JSON_Param = HelperFunction.getAdapter(runTimeId, name, "Interface_param_JSON");
		for (JsonNode jsonNode : JSON_Param) {
			String jsonName = HelperFunction.searchForParameter(jsonNode, "Name");
			String location = HelperFunction.searchForParameter(jsonNode, "Location");
			String params = "Set_Interface_Param_JSON /" + jsonName + "/" + location;
			params = params.replace("\"", "");
			ArrayNode returnValues = sendRequest(params);
			HelperFunction.updateAdapterReturnValue(runTimeId, name, returnValues, "Interface_param_JSON");

		}
	}

	// read excel adapter parameter and connect to it
	public static void readInterfaceParamEXCEL(String runTimeId, String name)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		ArrayNode EXCEL_Param = HelperFunction.getAdapter(runTimeId, name, "Interface_param_EXCEL");
		for (JsonNode json : EXCEL_Param) {
			String jsonName = HelperFunction.searchForParameter(json, "Name");
			String location = HelperFunction.searchForParameter(json, "Location");
			String category = HelperFunction.searchForParameter(json, "Category");
			String params = "Set_Interface_Param_Excel /" + jsonName + "/" + location + "/" + category;
			params = params.replace("\"", "");
			ArrayNode returnValues = sendRequest(params);
			HelperFunction.updateAdapterReturnValue(runTimeId, name, returnValues, "Interface_param_EXCEL");
		}
	}

	// read PDF adapter parameter and connect to it
	public static void readInterfaceParamFiles(String runTimeId, String name)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		ArrayNode Interface_param_File = HelperFunction.getAdapter(runTimeId, name, "Interface_param_File");
		for (JsonNode jsonNode : Interface_param_File) {
			String jsonName = HelperFunction.searchForParameter(jsonNode, "Name");
			String location = HelperFunction.searchForParameter(jsonNode, "Location");
			String category = HelperFunction.searchForParameter(jsonNode, "Category");
			String params = "Set_Interface_Param_File_Source /" + jsonName + "/" + location + "/" + category;
			params = params.replace("\"", "");
			ArrayNode returnValues = sendRequest(params);
			HelperFunction.updateAdapterReturnValue(runTimeId, name, returnValues, "Interface_param_File");
		}
	}

	// read rest api adapter parameter and connect to it
	public static void readInterfaceParamRestAPI(String runTimeId, String name)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		ArrayNode RestAPI_Param = HelperFunction.getAdapter(runTimeId, name, "Interface_param_RestAPI");
		ArrayNode values = HelperFunction.createArrayNode();
		for (JsonNode jsonNode : RestAPI_Param) {
			String jsonName = HelperFunction.searchForParameter(jsonNode, "Name");
			String location = HelperFunction.searchForParameter(jsonNode, "Location");
			String value = null;
			HelperFunction.updateAdapterReturnValue(runTimeId, name, values, "Interface_param_RestAPI");
		}
	}
//    @GetMapping(value = "Set_Interface_Param_ESI_REST /{name} /{URL} /{intrefaceType} /{commercialName} /{dataSourceType} ")

	private static ArrayNode sendRequest(String params) {
		// TODO Auto-generated method stub
		// define request
		try {
			URL url = new URL("http://localhost:8080/" + params);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// set connection configuration
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			int status = connection.getResponseCode();
			if (status > 299) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
				String response = responseContent.toString();
				JsonNode jsonNode = HelperFunction.convertStringToJsonNode(response);
				ArrayNode arrayNode = (ArrayNode) jsonNode.get("values");
				return arrayNode;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
