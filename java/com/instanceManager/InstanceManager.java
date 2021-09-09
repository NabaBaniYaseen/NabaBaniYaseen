package com.instanceManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.dataBaseManager.DBDigitalTwinManager;
import com.dataBaseManager.DBRobotManager;
import com.dataBaseManager.DataBaseFunctions;
import com.entities.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.jsonOperation.JsonReader;
import com.jsonOperation.JsonWriter;

public class InstanceManager extends DataBaseFunctions {
	JsonReader reader = new JsonReader();
	static JsonWriter writer = new JsonWriter();
	static DBRobotManager dBRobotManager = new DBRobotManager();
	static DBDigitalTwinManager dBDigitalTwinManager = new DBDigitalTwinManager();

	/*
	 * this method will call from sequencer when user want to create instance from
	 * any type of robot . first we will read the type of robot then call the method
	 * that responsible for create instance of this type of robot.
	 */
	public static CompletableFuture<String> createRobotInstance(String type, String definitionId, String robotName)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		type = type.replaceAll("\"", "");
		if (type.equals("IntegratorRobot")||type.equals("integratorRobot")) {
			return CompletableFuture.completedFuture(createIntegratorInstance(definitionId, robotName));
		} else if (type.equals("PredictorRobot")||type.equals("predictorRobot")) {
			return CompletableFuture.completedFuture(createPredictorInstance(definitionId, robotName));
		} else if (type.equals("LogicalRobot")||type.equals("logicalRobot")) {
			return CompletableFuture.completedFuture(createLogicalInstance(definitionId, robotName));
		}
		return null;
	}

	// for create integrator instance
	public static String createIntegratorInstance(String definitionId, String robotName) {
		// TODO Auto-generated method stub
		IntegratorRobot integratorRobot;
		try {
			// call j2E container and create a bean
			integratorRobot = RobotContainer.createIntegratorInstance(definitionId, robotName).get();
			// Here I call saveInstanceOnDB method to create row in transit data
			// table in database
			JsonNode json = saveInstanceOnDB(definitionId, integratorRobot.getRobotRunTimeId(), robotName,
					"integrator_robots");
			// get robot definition from DB Manager and Initlize bean instance
			setValuesOnIntegratorInstance(integratorRobot, definitionId, integratorRobot.getRobotRunTimeId(), robotName,
					json);
			return integratorRobot.getRobotRunTimeId();
		} catch (JsonProcessingException | InterruptedException | ExecutionException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "the process doesn't complete correctly";
	}

	// for create predictor instance
	public static String createPredictorInstance(String definitionId, String robotName) {
		PredictorRobot predictorRobot;
		try {
			// call j2E container and create a bean
			predictorRobot = RobotContainer.createPredictorInstance(definitionId, robotName).get();
			// Here I call saveInstanceOnDB method to create row in transit data
			// table in database
			JsonNode json = saveInstanceOnDB(definitionId, predictorRobot.getRobotRunTimeId(), robotName,
					"predictor_robots");
			// get robot definition from DB Manager and Initlize bean instance
			setValuesOnPredictorRobotInstance(predictorRobot, definitionId, predictorRobot.getRobotRunTimeId(),
					robotName, json);
			return predictorRobot.getRobotRunTimeId();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "the process doesn't complete correctly";
	}

	// for create logical flow instance
	public static String createLogicalInstance(String definitionId, String robotName) {
		// TODO Auto-generated method stub
		LogicalRobot logicalRobot;
		try {
			// call j2E container and create a bean
			logicalRobot = RobotContainer.createLogicFlowInstance(definitionId, robotName).get();
			// Here I call saveInstanceOnDB method to create row in transit data
			// table in database
			JsonNode json = saveInstanceOnDB(definitionId, logicalRobot.getRobotRunTimeId(), robotName,
					"logical_robots");
			// get robot definition from DB Manager and Initlize bean instance
			setValuesOnLogicalRobotInstance(logicalRobot, definitionId, logicalRobot.getRobotRunTimeId(), robotName,
					json);
			return logicalRobot.getRobotRunTimeId();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "the process doesn't complete correctly";
	}

	// for create logical flow instance
	public static CompletableFuture<DigitalTwin> createDigitalTwinInstance(String definitionId,
			String digitalTwinName) {
		// TODO Auto-generated method stub
		CompletableFuture<DigitalTwin> digitalTwin;
		try {
			// call j2E container and create a bean
			digitalTwin = RobotContainer.createDigitalTwinInstance(definitionId, digitalTwinName);
			// Here I call saveInstanceOnDB method to create row in transit data
			// table in database
			JsonNode json = saveInstanceOnDB(definitionId, digitalTwin.get().getDigitalTwinRuntimeId(), digitalTwinName,
					"digital_twin");
			// get digitalTwin definition from DB Manager and Initlize bean instance
			setValuesOnDigitalTwinInstance(digitalTwin.get(), definitionId, digitalTwin.get().getDigitalTwinRuntimeId(),
					digitalTwinName, json);
			return digitalTwin;

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// to set atttribute values for instance fron IntegratorRopot class
	public static IntegratorRobot setValuesOnIntegratorInstance(IntegratorRobot integratorRobot, String definiationId,
			String runTimeId, String robotName, JsonNode json) throws SQLException, JsonProcessingException {
		integratorRobot.setRobotRunTimeId(runTimeId);
		integratorRobot.setRobotDefinitionId(definiationId);
		integratorRobot.setRobotName(robotName);
		writer.updateRunTimeId(json, runTimeId);
		writer.updateRobotName(json, robotName);
		integratorRobot.setJson(json);
		return integratorRobot;
	}

	/*
	 * this method used to delete row (instance) from transit_data table in database
	 */
	public static boolean deleteInstance(String runTimeId) throws SQLException, JsonProcessingException {
		PreparedStatement ps = getPreparedStatement("DELETE FROM transit_data WHERE run_time_id=?");
		ps.setString(1, runTimeId);
		return ps.execute();
	}

	/* to set atttribute values in LogicalRobot instance */
	public static LogicalRobot setValuesOnLogicalRobotInstance(LogicalRobot logicalRobot, String definitionId,
			String runTimeId, String robotName, JsonNode json) {
		// TODO Auto-generated method stub
		try {
			logicalRobot.setRobotRunTimeId(runTimeId);
			logicalRobot.setRobotDefinitionId(definitionId);
			logicalRobot.setRobotName(robotName);
			json = dBRobotManager.getFullFile(definitionId, "logical_robots").get();
			writer.updateRunTimeId(json, runTimeId);
			writer.updateRobotName(json, robotName);
			logicalRobot.setJson(json);
			return logicalRobot;
		} catch (JsonMappingException | InterruptedException | ExecutionException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// to save instance in transit_data table in DB
	public static JsonNode saveInstanceOnDB(String definitionId, String runTimeId, String name, String tableName) {
		// TODO Auto-generated method stub
		JsonNode json = null;
		PreparedStatement ps;
		try {
			if (tableName.equals("digital_twin")) {
				json = dBDigitalTwinManager.getFullFile(definitionId).get();
				writer.updateDigitalTwinRunTimeId(json, runTimeId);
				writer.updateDigitalTwinName(json, name);
			} else {
				json = dBRobotManager.getFullFile(definitionId, tableName).get();
				writer.updateRunTimeId(json, runTimeId);
				writer.updateRobotName(json, name);
			}
			String jsonString = JsonWriter.convertJsonToString(json);
			if (tableName.equals("digital_twin")) {
				ps = getPreparedStatement("INSERT INTO digital_twin_instance VALUES(?,?,?,?,?)");
				ps.setString(1, definitionId);
				ps.setString(2, runTimeId);
				ps.setString(3, name);
				ps.setString(4, jsonString);
				ps.setInt(5, 1);
			} else {
				ps = getPreparedStatement("INSERT INTO transit_data VALUES(?,?,?)");
				ps.setString(1, runTimeId);
				ps.setString(2, definitionId);
				ps.setString(3, jsonString);
			}
			ps.execute();
		} catch (SQLException | InterruptedException | ExecutionException | JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	// to set atttribute values for instance fron PredictorRobot class
	public static PredictorRobot setValuesOnPredictorRobotInstance(PredictorRobot predictorRobot, String definitionId,
			String runTimeId, String name, JsonNode json) {
		// TODO Auto-generated method stub
		try {
			predictorRobot.setRobotRunTimeId(runTimeId);
			predictorRobot.setRobotDefinitionId(definitionId);
			predictorRobot.setRobotName(name);
			json = dBRobotManager.getFullFile(definitionId, "predictor_robots").get();
			writer.updateRunTimeId(json, runTimeId);
			writer.updateRobotName(json, name);
			predictorRobot.setJson(json);
			return predictorRobot;
		} catch (JsonMappingException | InterruptedException | ExecutionException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// to set atttribute values for instance fron DigitalTwin class
	public static DigitalTwin setValuesOnDigitalTwinInstance(DigitalTwin digitalTwin, String definitionId,
			String runTimeId, String name, JsonNode json) {
		try {
			digitalTwin.setDigitalTwinRuntimeId(runTimeId);
			digitalTwin.setDigitalTwinId(definitionId);
			digitalTwin.setDigitalTwinName(name);
			json = dBDigitalTwinManager.getFullFile(definitionId).get();
			writer.updateDigitalTwinRunTimeId(json, runTimeId);
			writer.updateDigitalTwinName(json, name);
			digitalTwin.setJson(json);

			return digitalTwin;
		} catch (JsonProcessingException | InterruptedException | ExecutionException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * this method will call from sequencer when user want to delete instance from
	 * any type of robot in this method we will read the type of robot and call the
	 * method that responsible for delete instance of this type of robot then remove
	 * instance from DB
	 */

	public static CompletableFuture<String> deleteRobotInstance(String runTimeId, String name, String type)
			throws JsonProcessingException, SQLException {
		// TODO Auto-generated method stub
		deleteInstance(runTimeId);
		if (type.equals("IntegratorRobot")) {
			return RobotContainer.deleteIntegratorInstance(runTimeId, name);
		} else if (type.equals("PredictorRobot")) {
			return RobotContainer.deletePredictorInstance(runTimeId, name);
		} else {
			return RobotContainer.deleteLogicalInstance(runTimeId, name);
		}
	}

	// to delete digital twin bean && delete instance from DB
	public static CompletableFuture<String> deleteDigitalTwinInstance(String runTimeId, String name, String type)
			throws JsonProcessingException, SQLException {
		// TODO Auto-generated method stub
		if (deleteInstance(runTimeId)) {
			return RobotContainer.deleteDigitalTwinInstance(runTimeId, name);
		}
		return null;
	}

}
