package com.runTimeController;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.script.ScriptException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class RunTimeController {

	public static CompletableFuture<String> startRunRobot(String runTimeId, String robotName, String type, String previousRunTimeId,
			String previousRobotName, String callType, boolean isLastRobot, ArrayNode actionParams)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException, ScriptException {
		// TODO Auto-generated method stub
		type = type.replaceAll("\"", "");
		if (type.equals("logicalRobot")||type.equals("logical_robots")) {
			return CompletableFuture.completedFuture(LogicalArithmeticExecutor.readFunction(runTimeId, robotName, previousRunTimeId, previousRobotName,callType,isLastRobot, actionParams));
		} else if (type.equals("IntegratorRobots")||(type.equals("integrator_robots"))) {
			IntegratorExecutor.readInterfaceParam(runTimeId, robotName);
			return null;
		} else if (type.equals("PredictorRobot")||type.equals("predictor_robots")) {
			return PredictorExecutor.readAPI(runTimeId, robotName,callType,isLastRobot,actionParams);
		}else if(type.equals("PresentorRobot")||type.equals("presentor_robots")) {
			return Presentor.readPresentorRobot(runTimeId, robotName);
		}
		else {
			return null;
		}
	}

}
