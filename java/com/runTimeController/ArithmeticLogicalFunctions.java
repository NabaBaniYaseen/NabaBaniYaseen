package com.runTimeController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jsonOperation.JsonOperation;

public class ArithmeticLogicalFunctions {
	// run premetive function
	public static String runSimpleFunction(String simpleFunction, ArrayNode inputAtrray) {
		// define engine to evalute string equation

		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine engine = scriptEngineManager.getEngineByName("JavaScript");
		simpleFunction = replaceDataType(simpleFunction);
		/*
		 * loop through input and replace all input in string equation with its values
		 */
		System.out.println(inputAtrray);
		String str="";
		for (JsonNode input : inputAtrray) {
			String variableName = HelperFunction.searchForString(input,"name");
			String value = HelperFunction.searchForString(input,"value");
//			engine.eval("var "+variableName+"= "+value);
			str+="var "+variableName+"= "+value+";";
//			Iterator<Entry<String, JsonNode>> iterator = ((JsonNode) input).fields();
//			while (iterator.hasNext()) {
//				Entry<String, JsonNode> entry = iterator.next();
//				String inputValue = JsonOperation.convertJsonToString(entry.getValue());
//				System.out.println(entry.getKey());
//				System.out.println(entry.getValue());
//
//				simpleFunction = simpleFunction.replaceAll(entry.getKey(), inputValue);
//			}
		}
		// evaluate equation and return the output from it
//		try {
			str+=simpleFunction;
				try {
					System.out.println(str);
					return engine.eval(str).toString();
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
// engine.eval("5+6").toString();
//		} catch (ScriptException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
			return str;
	}

	// this method used to excute open source method
	public static void runComplexFunction(String openSourceFunction, ArrayNode input) {
		// I will add implementation for this method latter
		List<String> inputList = HelperFunction.convertArrayNodeToArray(input);
		String output = OpenSource.callOpenSource(openSourceFunction, new ArrayList<String>(inputList),
				inputList.size(), 0, 0);

	}

	// to run user defiened algorithem
	public static void runUserDefiendEquation(String userDefinedAlgorithmValue, ArrayNode input) {
		// I will add implementation for this method latter
//		runSimpleFunction(userDefinedAlgorithmValue, input);
	}

	public static void runUserDefiendScript(String userDefinedAlgorithmValue, ArrayNode input) {
		// I will add implementation for this method latter
//		runSimpleFunction(userDefinedAlgorithmValue, input);
	}

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "{\"x\":5,\"y\":12}";
		JsonNode actualObj = mapper.readTree(jsonString);
		ArrayNode input = mapper.createArrayNode();
		input.add(actualObj);
	}

//	static public String replace(String variableName, String value, String function) {
////		function = "var " + variableName + " =" + value + "; " + function;
//		System.out.println(variableName);
//		int variableLength = variableName.length();
//		for (int i = 0; i < function.length(); i++) {
//			if (variableName.equals(function.substring(i, i + variableLength))) {
//				boolean flag = true;
//
//				if (function.substring(i + variableLength).equals("=")
//						&& (!(function.substring(i + variableLength + 1).equals("=")))) {
//					flag = false;
//				}
//				String[] assignOperators = { "++", "--", "+=", "-=", "*=", "/=", "%=" };
//				for (int j = 0; j < assignOperators.length; j++) {
//					if ((function.substring(i + variableLength, i + variableLength + 1).equals(assignOperators[j]))) {
//						flag = false;
//					}
//				}
//				if (function.substring(i - 2, i).equals("--")
//						|| (function.substring(i + variableLength).equals("++"))) {
//					flag = false;
//				}
//				if (flag) {
//					function = function.substring(0, i) + value
//							+ function.substring(i + variableLength, function.length());
//				} else {
//					function = "var " + variableName + " =" + value + ";/n" + function;
//				}
//
//			}
//		}
//		return function;
//	}

	static public String replaceDataType(String simpleFunction) {
		// TODO Auto-generated method stub
		simpleFunction = simpleFunction.replaceAll("int", "var").replaceAll("double", "var")
				.replaceAll("boolean", "var").replaceAll("char", "var").replaceAll("byte", "var")
				.replaceAll("short", "var").replaceAll("long", "var").replaceAll("float", "var")
				.replace("sin", "Math.sin").replace("cos", "Math.cos").replace("tan", "Math.tan")
				.replace("sqrt", "Math.sqrt").replace("sqr", "Math.pow").replace("log", "Math.log");

		return simpleFunction;
	}
}
