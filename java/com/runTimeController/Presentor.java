package com.runTimeController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.instanceManager.InstanceManager;
import com.instanceManager.InstanceRunTimeManager;

public class Presentor {
	public static CompletableFuture<String> readPresentorRobot(String runTimeId, String robotName)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		JsonNode json = HelperFunction.getPresentor(runTimeId, robotName);
		String type = HelperFunction.searchForString(json, "type");
		String refreshRate = HelperFunction.searchForString(json, "refresh_rate");
		String name = HelperFunction.searchForString(json, "name");
		ArrayNode parameters = HelperFunction.getArrayNode(json, "parameter");
		ArrayNode digitalTwin = HelperFunction.getArrayNode(json, "digital_twin");

		createCsvFile(name, parameters, digitalTwin);
//		"digital_twin": [
//			"digital_twin_run_time_id": "",
//			"parameter": [{
//				"name": "",
//				"value": ""
//			}]
//		}],

		return null;
		// TODO Auto-generated method stub

	}

	private static void createCsvFile(String name, ArrayNode parameters, JsonNode digitalTwins)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		try {
			PrintWriter pw = new PrintWriter(new File("C:\\Users\\LENOVO\\Desktop\\excel system\\" + name + ".csv"));
			StringBuilder sb = new StringBuilder();
			for (JsonNode param : parameters) {
				String paramName = HelperFunction.searchForString(param, "name");
				sb.append(paramName);
			}
			sb.append("\r\n");
			for (JsonNode json : digitalTwins) {
				String runTimeId = HelperFunction.searchForValues(json, "digital_twin_run_time_id");
				String digitalTwinName = HelperFunction.searchForValues(json, "name");
				JsonNode digitalTwinJson = InstanceRunTimeManager.getDigitalTwinInstance(runTimeId, digitalTwinName);
				ArrayNode robots = HelperFunction.readRopotSequence(digitalTwinJson);
				for (JsonNode robot : robots) {
					String robotRunTimeId = HelperFunction.searchForString(robot, "robot_runtime_id");
					String Name = HelperFunction.searchForString(robot, "Name");
					String robotType = HelperFunction.searchForString(robot, "RobotType");
					JsonNode Json = InstanceRunTimeManager.getInstance(robotRunTimeId, Name, robotType);
					String output=HelperFunction.searchForString(HelperFunction.getFunctionsOutputJson(json),"Value");
					sb.append(output);
				}
				sb.append("\r\n");
			}
			pw.write(sb.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
