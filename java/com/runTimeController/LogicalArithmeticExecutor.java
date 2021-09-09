package com.runTimeController;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import javax.script.ScriptException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LogicalArithmeticExecutor {
	// used for read function definition

	static public String readFunction(String runTimeId, String name, String previousRunTimeId, String previousRobotName,
			String callType, boolean isLastRobot, ArrayNode actionParams)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException, ScriptException {
		// TODO Auto-generated method stub
		ArrayNode functions = HelperFunction.getFunctions(runTimeId, name);
		System.out.println("inside read function");
		if (callType.equalsIgnoreCase("ActionCall")) {
			actionParams = dellWithLabel(actionParams, previousRunTimeId, previousRobotName);
			return readFunctionThatCalledByActionCall(functions, actionParams);
		}
		for (JsonNode function : functions) {
			ArrayNode input = HelperFunction.getFunctionsInput(function);
			if (previousRunTimeId != null) {
				input = dellWithLabel(input, previousRunTimeId, previousRobotName);
			}
			String operationType = HelperFunction.getOperationType(function);
			switch (operationType) {
			case "Simple":
				String simpleFunction = HelperFunction.getOperationValue(function);
				System.out.println(ArithmeticLogicalFunctions.runSimpleFunction(simpleFunction, input));
				break;
			case "Complex":
				String openSourceFunction = HelperFunction.getOperationValue(function);
				ArithmeticLogicalFunctions.runComplexFunction(openSourceFunction, input);
				break;
			case "UserDefiened":
				JsonNode userDefinedAlgorithm = HelperFunction.searchForFunction(function, "user_defined_algorithm");
				String userDefinedAlgorithmType = HelperFunction.getUserDefienedFunctionType(userDefinedAlgorithm);
				String userDefinedAlgorithmValue = HelperFunction.getUserDefienedFunctionValue(function);
				if (userDefinedAlgorithmType.equals("Equation")) {
					ArithmeticLogicalFunctions.runUserDefiendEquation(userDefinedAlgorithmValue, input);
				} else {
					ArithmeticLogicalFunctions.runUserDefiendScript(userDefinedAlgorithmValue, input);
				}
				break;
			}
//			String actionType = HelperFunction.getActions(jsonNode);
//			String actionReturnValue = ActionExcutor.dealWithActions(jsonNode, actionType);
//			if(actionType.equals("replay back")) {
//				return actionReturnValue;
//			}
			ExcuteAction.readActions(function, callType, isLastRobot);
		}
		return null;
	}

	private static String readFunctionThatCalledByActionCall(ArrayNode functions, ArrayNode actionParams) throws ScriptException {
		// TODO Auto-generated method stub
		for (JsonNode function : functions) {
			ArrayNode inputs = HelperFunction.getFunctionsInput(function);
			boolean flag = true;
			boolean foundInput = false;
			for (JsonNode input : inputs) {
				foundInput = false;
				for (JsonNode actionParameter : actionParams) {
					if (HelperFunction.searchForString(input, "name")
							.equals(HelperFunction.searchForString(actionParameter, "name"))) {
						foundInput = true;
					}
				}
				flag = flag && foundInput;
			}
			if (flag) {
				String operationType = HelperFunction.getOperationType(function);
				switch (operationType) {
				case "Simple":
					String simpleFunction = HelperFunction.getOperationValue(function);
					ArithmeticLogicalFunctions.runSimpleFunction(simpleFunction, inputs);
					break;
				case "Complex":
					String openSourceFunction = HelperFunction.getOperationValue(function);
					ArithmeticLogicalFunctions.runComplexFunction(openSourceFunction, inputs);
					break;
				case "UserDefiened":
					JsonNode userDefinedAlgorithm = HelperFunction.searchForFunction(function,
							"user_defined_algorithm");
					String userDefinedAlgorithmType = HelperFunction.getUserDefienedFunctionType(userDefinedAlgorithm);
					String userDefinedAlgorithmValue = HelperFunction.getUserDefienedFunctionValue(function);
					if (userDefinedAlgorithmType.equals("Equation")) {
						ArithmeticLogicalFunctions.runUserDefiendEquation(userDefinedAlgorithmValue, inputs);
					} else {
						ArithmeticLogicalFunctions.runUserDefiendScript(userDefinedAlgorithmValue, inputs);
					}
					break;
				}
//			String actionType = HelperFunction.getActions(jsonNode);
//			String actionReturnValue = ActionExcutor.dealWithActions(jsonNode, actionType);
//			if(actionType.equals("replay back")) {
//				return actionReturnValue;
//			}
				ExcuteAction.readActions(function, "ActionCall", false);
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static ArrayNode dellWithLabel(ArrayNode inputs, String previousRunTimeId, String previousRobotName) {
		// TODO Auto-generated method stub
		ArrayNode functions = HelperFunction.getFunctions(previousRunTimeId, previousRobotName);
		for (JsonNode input : inputs) {
			String inputLabel = HelperFunction.getLabel(input);
			for (JsonNode jsonNode : functions) {
				ArrayNode outputOfPreviousRobot = HelperFunction.getFunctionsOutput(jsonNode);
				for (JsonNode outputOfRobot : outputOfPreviousRobot) {
					JsonNode value = HelperFunction.searchOnJson(outputOfRobot, inputLabel);
					if (value != null) {
						((ObjectNode) input).put("value", value);
					}
				}
			}
		}
		return inputs;
	}

}
