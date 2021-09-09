package com.runTimeController;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jsonOperation.JsonReader;

public class PredictorExecutor {
	public static CompletableFuture<String> readAPI(String runTimeId, String name, String callType, boolean isLastRobot, ArrayNode actionParams) {
		// TODO Auto-generated method stub
		ArrayNode functions = HelperFunction.getFunctions(runTimeId, name);
		for (JsonNode jsonNode : functions) {
			String APIName = HelperFunction.searchForValues(jsonNode, "APIName");
			String APIURL = HelperFunction.searchForValues(jsonNode, "APIURL");

			ArrayNode Parameters = (ArrayNode) JsonReader.searchForEntityy(jsonNode, "Parameters");
			 AIExecution.callApi(APIName, APIURL, Parameters);
		}
		return null;
	}

}