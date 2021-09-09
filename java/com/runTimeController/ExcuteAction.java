package com.runTimeController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import javax.script.ScriptException;

import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sequencer.Sequencer;

public class ExcuteAction {
	// to read action type
	public static boolean readActions(JsonNode function, String callType, boolean isLastRobot) {
		// TODO Auto-generated method stub
		String actionType = HelperFunction.getAction(function).replaceAll("\"", "");
		if (actionType.equalsIgnoreCase("replay back")) {
			if (callType.equalsIgnoreCase("synchronous") && isLastRobot) {
				JsonNode output = HelperFunction.searchForKeyValue(function, "output");
				return callExternalSystem(function, output, callType);
			} else {
				return false;
			}
		} else if (actionType.equalsIgnoreCase("Excute robot")) {
			JsonNode action = HelperFunction.searchForKeyValue(function, "excute_robot");
			excuteRobotAction(action);
		} else if (actionType.equals("Excute digital twin")) {
			JsonNode action = HelperFunction.searchForKeyValue(function, "excute_digital_twin");
			excuteDigitalTwinAction(action);
		} 
		return true;
	}
	
	
	// in this action type we need to call createRobotInstance method from sequencer
	private static void excuteRobotAction(JsonNode action) {
		// TODO Auto-generated method stub
		String robotName = HelperFunction.searchForString(action, "robot_name").replaceAll("\"", "");
		String robotDefinitionId = HelperFunction.searchForString(action, "robot_definition_id").replaceAll("\"", "");
		String robotType = HelperFunction.searchForString(action, "robot_type").replaceAll("\"", "");
		ArrayNode actionInputPrameters = HelperFunction.getArrayNode(null, "action_input_prameters");
		try {
			Sequencer.createRobotInstance(robotType, robotDefinitionId, robotName, null, null, "ActionCall", false,
					actionInputPrameters);
		} catch (JsonProcessingException | InterruptedException | ExecutionException | SQLException
				| ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// to call external system using all types of robot
	private static boolean callExternalSystem(JsonNode function, JsonNode output, String callType) {
		// TODO Auto-generated method stub
		JsonNode externalSystem = HelperFunction.searchForKeyValue(function, "external_system");
		String adapterType = HelperFunction.searchForString(externalSystem, "AdapterType");
		String APItype = HelperFunction.searchForString(externalSystem, "APItype");
		String externalSystemName = HelperFunction.searchForString(externalSystem, "External_System_Name");
		String username = HelperFunction.searchForString(externalSystem, "Username");
		String password = HelperFunction.searchForString(externalSystem, "Password");
		String params = null;
		if (adapterType.equalsIgnoreCase("SOAP")) {
			JsonNode SOAP = HelperFunction.searchForKeyValue(externalSystem, "SOAPParam");
			params = callSOAP(SOAP, APItype, externalSystemName, username, password, APItype,
					HelperFunction.convertJsonNodeToString(output), callType);
		} else if (adapterType.equalsIgnoreCase("REST")) {
			JsonNode REST = HelperFunction.searchForKeyValue(externalSystem, "RESTParam");
			params = callREST(REST, APItype, externalSystemName, username, password,
					HelperFunction.convertJsonNodeToString(output), callType);
		} else if (adapterType.equalsIgnoreCase("Generic")) {
			JsonNode GenericAPIParam = HelperFunction.searchForKeyValue(externalSystem, "GenericAPIParam");
			params = GenericAPI(GenericAPIParam, APItype, externalSystemName, username, password,HelperFunction.convertJsonNodeToString(output), callType);
		}
		return sendRequest(params, HelperFunction.convertJsonNodeToString(output));
	}

	// generate params of GenericAPI,
	private static String GenericAPI(JsonNode genericAPIParam, String aPItype, String externalSystemName,
			String username, String password, String output, String callType) {
		// TODO Auto-generated method stub
		String requestParam ;
		String requestParamType;
		if (callType.equalsIgnoreCase("ActionCall")) {
			requestParam = output;
			requestParamType = "JSON-STRING";
		}else {
		 requestParam = HelperFunction.searchForString(genericAPIParam, "requestParam").replaceAll("\"", "");
		 requestParamType = HelperFunction.searchForString(genericAPIParam, "requestParamType").replaceAll("\"",
				"");
		}
		String IPAddress = HelperFunction.searchForString(genericAPIParam, "IPAddress").replaceAll("\"", "");
		String authflag = HelperFunction.searchForString(genericAPIParam, "Authflag").replaceAll("\"", "");
		String port = HelperFunction.searchForString(genericAPIParam, "port").replaceAll("\"", "");
		String APICallName = HelperFunction.searchForString(genericAPIParam, "APICallName").replaceAll("\"", "");
		return "Set_Interface_Param_ESI_Generic /" + externalSystemName + "/" + aPItype + "/" + requestParam + "/"
				+ requestParamType + "/" + authflag + "/" + username + "/" + password + IPAddress + "/" + port + "/"
				+ APICallName;
	}

	// generate params of callREST
	private static String callREST(JsonNode REST, String aPItype, String externalSystemName, String username,
			String password, String output, String callType) {
		// TODO Auto-generated method stub
		String requestParam;
		String requestParamType;

		if (callType.equalsIgnoreCase("ActionCall")) {
			requestParam = output;
			requestParamType = "JSON-STRING";
		} else {
			requestParam = HelperFunction.searchForString(REST, "requestParam").replaceAll("\"", "");
			requestParamType = HelperFunction.searchForString(REST, "requestParamType").replaceAll("\"", "");
		}
		String externalSystemURL = HelperFunction.searchForString(REST, "External_System_URL");

		return "Set_Interface_Param_ESI_REST /" + externalSystemName + "/" + externalSystemURL + "/" + aPItype + "/"
				+ requestParam + "/" + requestParamType;
	}

	// generate params of callSOAP
	private static String callSOAP(JsonNode SOAP, String aPItype, String externalSystemName, String externalSystemURL,
			String username, String password, String output, String callType) {
		// TODO Auto-generated method stub
		String xml;
		if (callType.equalsIgnoreCase("ActionCall")) {
			JSONObject json = new JSONObject(output);
			xml = XML.toString(json);
		} else {
			xml = HelperFunction.searchForString(SOAP, "XML").replaceAll("\"", "");
		}
		String wsdl = HelperFunction.searchForString(SOAP, "WSDL").replaceAll("\"", "");
		String SOAPAction = HelperFunction.searchForString(SOAP, "SOAPAction").replaceAll("\"", "");
		return "Set_Interface_Param_ESI_SOAP /" + wsdl + "/" + xml + "/" + SOAPAction + "/" + externalSystemName + "/"
				+ aPItype;
		// O synchronous 1 asynchronous
	}

	// send request to external system
	private static boolean sendRequest(String params, String output) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL("http://localhost:8080/" + params);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
//			con.setRequestProperty("Content-Type", "application/json; utf-8");
//			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
//			try (OutputStream os = con.getOutputStream()) {
//				byte[] input = output.getBytes("utf-8");
//				os.write(input, 0, input.length);
//			}
			int status = con.getResponseCode();
			if (status <= 299) {
				// add return value on jsom file
				return true;
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return false;
	}

	// in this action type we need to call createDigitalTwinInstance method from
	// sequencer
	private static void excuteDigitalTwinAction(JsonNode action) {
		// TODO Auto-generated method stub
		String definitionId = HelperFunction.searchForString(action, "digital_twin_id").replaceAll("\"", "");
		String digitalTwinName = HelperFunction.searchForString(action, "digital_twin_name").replaceAll("\"", "");

		try {
			Sequencer.createDigitalTwinInstance(definitionId, digitalTwinName, "ActionCall", null);
		} catch (JsonProcessingException | InterruptedException | ExecutionException | SQLException
				| ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//		String robot_name = HelperFunction.searchForString(json, "RobotName").replaceAll("\"", "");
//		String Robot_Run_Time_ID = HelperFunction.searchForString(json, "Robot_Run_Time_ID").replaceAll("\"", "");
//		JsonNode action = HelperFunction.searchForKeyValue(json, "Actions");
//		if (action_type.equals("Excute robot")) {
//			action = HelperFunction.searchForKeyValue(json, "excute_robot");
//			excuteDigitalTwinAction(action);
//		} else if (action_type.equals("Excute digital twin")) {
//			action = HelperFunction.searchForKeyValue(json, "excute_digital_twin");
//			excuteDigitalTwinAction(action);
//		} else if (action_type.equals("Show Report")) {
//			excuteShowReport(action);
//		} else if (action_type.equals("Show Dashboard")) {
//			excuteShowDashboard(action);
//		} else if (action_type.equals("replay back")) {
//			action = HelperFunction.searchForKeyValue(action, "external_system");
//			JsonNode output = HelperFunction.searchForKeyValue(json, "output");
//			try {
//				excuteReplayBackAction(action, output);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//
//
//	// in this action type we need to call showDa
//	// PresentorExcutor class
//	private static void excuteShowDashboard(JsonNode action) {
//		// TODO Auto-generated method stub
//		String dashboardName = HelperFunction.searchForString(action, "dashboard_name").replaceAll("\"", "");
//		PresentorExcutor.showDashboard(dashboardName);
//	}
//
//	// in this action type we need to call showDashboard method from
//	// PresentorExcutor class
//	private static void excuteShowReport(JsonNode action) {
//		// TODO Auto-generated method stub
//		String reportName = HelperFunction.searchForString(action, "report_name").replaceAll("\"", "");
//		PresentorExcutor.showReport(reportName);
//
//	}
//
//
}
