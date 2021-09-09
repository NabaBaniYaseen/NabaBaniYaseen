package com.sequencer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dataBaseManager.DataBaseFunctions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.runTimeController.HelperFunction;

public class sequenceManager extends DataBaseFunctions {
	static public boolean manageDigitalTwinSequence(String runTimeId, String digitalTwinName, JsonNode nextDigitalTwin)
			throws SQLException {
		// TODO Auto-generated method stub
		String nextDigitalTwindefinitionId = HelperFunction.searchForValues(nextDigitalTwin, "definition_id").replaceAll("\"", "");
		String nextDigitalTwinName = HelperFunction.searchForValues(nextDigitalTwin, "Name").replaceAll("\"", "");
		String sequence = HelperFunction.searchForValues(nextDigitalTwin, "sequence").replaceAll("\"", "");
		if (!nextDigitalTwinName.equals("")) {
			PreparedStatement ps = getPreparedStatement("INSERT INTO digital-twin-sequence VALUES(?,?,?,?,?)");
			ps.setString(1, runTimeId);
			ps.setString(2, nextDigitalTwindefinitionId);
			ps.setString(3, digitalTwinName);
			ps.setString(4, sequence);
			ps.setString(5, nextDigitalTwinName);
			return ps.execute();
		}
		return true;
	}

	static public String[] getPreviousDigitalTwin(String definitionId) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = getPreparedStatement(
				"SELECT `digital-twin-run-time-id`  , `digital-twin-name` FROM `digital-twin-sequence` WHERE `next-digital-twin-definition-id` = ?");
		ps.setString(1, definitionId);
		ResultSet rs = ps.executeQuery();
		String[] previousDigitalTwin = { null, null };
		while (rs.next()) {
			previousDigitalTwin[0] = rs.getString("digital-twin-run-time-id");
			previousDigitalTwin[1] = rs.getString("digital-twin-name");
		}
		return previousDigitalTwin;
	}

	static public String[] getآNextDigitalTwin(String runTimeId) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = getPreparedStatement(
				"SELECT next-digital-twin-definition-id FROM digital-twin-sequence WHERE digital-twin-run-time-id = ?");
		ps.setString(1, runTimeId);
		ResultSet rs = ps.executeQuery();
		String[] nextDigitalTwinDefinitionId = { null, null };
		while (rs.next()) {
			nextDigitalTwinDefinitionId[0] = rs.getString("next-digital-twin-definition-id");
			nextDigitalTwinDefinitionId[1] = rs.getString("next-digital-twin-name");

		}
		return nextDigitalTwinDefinitionId;
	}

	public static int manageRobotSequence(ArrayNode sequenceOfRobot, String digitalTwinRunTimeId) throws SQLException {
		// TODO Auto-generated method stub
		int robotCount = 0;
		for (JsonNode nextRobot : sequenceOfRobot) {
			String robotName = HelperFunction.getRobot(nextRobot, "Name");
			String definitionId = HelperFunction.getRobot(nextRobot, "robot_deinition_id");
			String type = HelperFunction.getRobot(nextRobot, "RobotType");
			String robot_sequence = HelperFunction.getRobot(nextRobot, "robot_sequence");
			int seqeuence = Integer.parseInt(robot_sequence);
			PreparedStatement ps = getPreparedStatement("INSERT INTO `robot-sequence` VALUES (?,?,?,?,?,?)");
			ps.setString(1, definitionId);
			ps.setString(2, robotName);
			ps.setString(3, digitalTwinRunTimeId);
			ps.setInt(4, seqeuence);
			// robot run time Id still undefiened
			ps.setString(5, "");
			ps.setString(6, type);
			ps.execute();
			robotCount++;
		}
		updateRobotCountInDigitalTwin(digitalTwinRunTimeId, robotCount);
		return robotCount;
	}

	public static String[] getLastRobotInDigitalTwin(String digitalTwinRunTimeId, int robotCount) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = getPreparedStatement(
				"SELECT robot-run-time-id , robot-name FROM robot-sequence WHERE attached-digital-twin-run-time-id = ? and robot-sequence = ?");
		ps.setString(1, digitalTwinRunTimeId);
		ps.setInt(2, robotCount);
		ResultSet rs = ps.executeQuery();
		String[] lastRobot = { null, null };
		while (rs.next()) {
			lastRobot[0] = rs.getString("robot-run-time-id");
			lastRobot[1] = rs.getString("robot-name");
		}
		return lastRobot;
	}

	public static boolean updateRobotCountInDigitalTwin(String digitalTwinRunTimeId, int robotCount)
			throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = getPreparedStatement(
				"UPDATE `digital_twin_instance` SET robot_count =? WHERE digital_twin_runtime_id =?");
		ps.setInt(1, robotCount);
		ps.setString(2, digitalTwinRunTimeId);
		return ps.execute();
	}

}
