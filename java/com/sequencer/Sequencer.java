package com.sequencer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.script.ScriptException;

import com.dataBaseManager.DataBaseFunctions;
import com.entities.DigitalTwin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.instanceManager.InstanceManager;
import com.instanceManager.InstanceRunTimeManager;
import com.runTimeController.HelperFunction;
import com.runTimeController.RunTimeController;

public class Sequencer extends DataBaseFunctions {
	// this method will call from restful api to create digital twin instance
	public static CompletableFuture<String> createDigitalTwinInstance(String definitionId, String digitalTwinName,
			String callType, JsonNode values)
			throws InterruptedException, ExecutionException, JsonProcessingException, SQLException, ScriptException {
		// TODO Auto-generated method stub
		if (checkValuesOnDigitalTwinJson(definitionId, callType, values)) {
			System.out.println("stat 1");
			DigitalTwin digitalTwin = InstanceManager.createDigitalTwinInstance(definitionId, digitalTwinName).get();
			String runTimeId = digitalTwin.getDigitalTwinRuntimeId();
			JsonNode json = InstanceRunTimeManager.getDigitalTwinInstance(runTimeId, digitalTwinName);
			int robotCount = manageSequence(digitalTwinName, runTimeId, json);
			System.out.println("stat 1 2");
			runDigitalTwin(definitionId, runTimeId, digitalTwinName, robotCount, callType).get();
			return CompletableFuture.completedFuture(runTimeId);
		}
		return CompletableFuture.completedFuture(
				"the parameters and return values that send in event call are not the same with values that store in digital twin json file .");
	}

	/*
	 * test if the parameters and return values that send in event call are the same
	 * with values that store in digital twin json file .
	 */
	private static boolean checkValuesOnDigitalTwinJson(String definitionId, String callType, JsonNode values)
			throws JsonMappingException, JsonProcessingException, SQLException {
		// TODO Auto-generated method stub
		if (callType.equals("Synchronous")) {
			PreparedStatement ps = getPreparedStatement("SELECT json FROM digital_twin WHERE digital_twin_id = ?");
			ps.setString(1, definitionId);
			ResultSet rs = ps.executeQuery();
			JsonNode json;
			while (rs.next()) {
				json = HelperFunction.convertStringToJsonNode(rs.getString("json"));
			}
			ArrayNode parameters = HelperFunction.getArrayNode(values, "Parameters");
			ArrayNode ReturnValue = HelperFunction.getArrayNode(values, "ReturnValue");
			ArrayNode actualParameters = HelperFunction.getArrayNode(values, "Parameters");
			ArrayNode actualReturnValue = HelperFunction.getArrayNode(values, "ReturnValue");
			if (!parameters.toString().equals(actualParameters.toString())
					&& (!ReturnValue.toString().equals(actualReturnValue.toString()))) {
				return false;
			}
		}
		/*
		 * else the type will be Time so we we will not recive parameters and return
		 * values in json.
		 */
		return true;
	}

	public static int manageSequence(String digitalTwinName, String runTimeId, JsonNode json) throws SQLException {
		// TODO Auto-generated method stub
		// to set digital twin sequence
		JsonNode nextDigitalTwin = HelperFunction.searchForKeyValue(json);
		sequenceManager.manageDigitalTwinSequence(runTimeId, digitalTwinName, nextDigitalTwin);
		// set robot sequence in the digital twin
		ArrayNode sequenceOfRobot = HelperFunction.readRopotSequence(json);
		int robotCount = sequenceManager.manageRobotSequence(sequenceOfRobot, runTimeId);
		return robotCount;
	}

	// this method used to read sequence of robot in digital twin then work with
	// each robot in the sequence
	public static CompletableFuture<String> runDigitalTwin(String definitionId, String runTimeId,
			String digitalTwinName, int robotCount, String callType)
			throws JsonProcessingException, SQLException, ScriptException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		System.out.println("stat 2");
		String previousDigitalTwinRunTimeId = null;
		String previousDigitalTwinName = null;
		String previousRunTimeId = null;
		String previousRobotName = null;
		for (int i = 1; i <= robotCount; i++) {
			// 1) select where the definition Id of the current digital twin was next
			// definitionId
			if (i == 1) {
				// to get the previousRunTimeId and previousRobotName from the last digital twin
				String[] previousDigitalTwin = sequenceManager.getPreviousDigitalTwin(definitionId);
				previousDigitalTwinRunTimeId = previousDigitalTwin[0];
				previousDigitalTwinName = previousDigitalTwin[1];
				PreparedStatement ps = getPreparedStatement(
						"SELECT robot_count FROM digital_twin_instance WHERE digital_twin_runtime_id = ?");
				ps.setString(1, previousDigitalTwinRunTimeId);
				ResultSet rs = ps.executeQuery();
				int robotCountForPreviousDigitalTwin = 0;
				while (rs.next()) {
					robotCountForPreviousDigitalTwin = rs.getInt("robot_count");
				}
				if (robotCountForPreviousDigitalTwin != 0) {
					String[] LastRobotInPrevDigitalTwin = sequenceManager.getLastRobotInDigitalTwin(previousRunTimeId,
							robotCountForPreviousDigitalTwin);
					previousRunTimeId = LastRobotInPrevDigitalTwin[0];
					previousRobotName = LastRobotInPrevDigitalTwin[1];
				}
			} else {
				// get previousRunTimeId and previousRobotName for the robot
				PreparedStatement ps = getPreparedStatement(
						"SELECT robot-run-time-id , robot-name FROM robot-sequence WHERE attached-digital-twin-run-time-id = ? and robot-sequence = ?");
				ps.setString(1, runTimeId);
				ps.setInt(1, i - 1);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					previousRunTimeId = rs.getString("robot-run-time-id");
					previousRobotName = rs.getString("robot-name");
				}
			}
			String robotDefinitionId = null;
			String robotName = null;
			String type = null;
			PreparedStatement ps = getPreparedStatement(
					"SELECT `robot-definition-id` , `robot-name` , type FROM `robot-sequence` WHERE `attached-digital-twin-run-time-id`=? and `robot-sequence`=?");
			ps.setString(1, runTimeId);
			ps.setInt(2, i);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				robotDefinitionId = rs.getString("robot-definition-id");
				robotName = rs.getString("robot-name");
				type = rs.getString("type");
			}
			boolean isLastRobot = i == robotCount;
			CompletableFuture<String> returnValue = createRobotInstance(type, robotDefinitionId, robotName,
					previousRunTimeId, previousRobotName, callType,isLastRobot,null);// this robot not create by action call or this robot is created by normal sequence
			if (returnValue != null) {
				return returnValue;
			}
		}
		return null;
	}

	// this method will call from runDigitalTwinMethod to create robotInstance
	public static CompletableFuture<String> createRobotInstance(String type, String definitionId, String name,
			String previousRunTimeId, String previousRobotName, String callType, boolean isLastRobot,ArrayNode actionParams)
			throws InterruptedException, ExecutionException, JsonProcessingException, SQLException, ScriptException {
		// TODO Auto-generated method stub
		// call instance manger to create robot instance
		CompletableFuture<String> runTimeId = InstanceManager.createRobotInstance(type, definitionId, name);
		return runRobot(runTimeId.get(), name, type, previousRunTimeId, previousRobotName ,callType, isLastRobot, actionParams);
		// return runTimeId;
	}

	// this method used to call run tume controller
	public static CompletableFuture<String> runRobot(String runTimeId, String robotName, String type,
			String previousRunTimeId, String previousRobotName, String callType,boolean isLastRobot,ArrayNode actionParams)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException, ScriptException {
		// TODO Auto-generated method stub
		System.out.println("stat 11");
		return RunTimeController.startRunRobot(runTimeId, robotName, type, previousRunTimeId, previousRobotName,callType,isLastRobot ,actionParams);
	}

}
