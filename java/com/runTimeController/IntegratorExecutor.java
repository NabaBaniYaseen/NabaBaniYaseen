package com.runTimeController;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class IntegratorExecutor {
	// this method used to check what is the adapter that exist in json instance and
	// start run it
	public static void readInterfaceParam(String runTimeId, String name)
			throws JsonProcessingException, SQLException, InterruptedException, ExecutionException {
		// if data base adapter exist start read data base parameter
		if (checkDataBaseAdapterPresence(runTimeId, name)) {
			IntegratorLogicExecutor.readInterfaceParamDB(runTimeId, name);
		}
		if (checkJsonAdapterPresence(runTimeId, name)) {
			IntegratorLogicExecutor.readInterfaceParamJSON(runTimeId, name);
		}
		if (checkEXCELAdapterPresence(runTimeId, name)) {
			IntegratorLogicExecutor.readInterfaceParamEXCEL(runTimeId, name);
		}

		if (checkFileAdapterPresence(runTimeId, name)) {
			IntegratorLogicExecutor.readInterfaceParamRestAPI(runTimeId, name);
		}
	}

	private static boolean checkFileAdapterPresence(String runTimeId, String name) {
		// TODO Auto-generated method stub
		ArrayNode interfaceParamFiles = HelperFunction.getAdapter(runTimeId, name, "Interface_param_File");
		for (JsonNode jsonNode : interfaceParamFiles) {
			String value = HelperFunction.searchForParameter(jsonNode, "Name");
			if (!value.equals("\"\"")) {
				return true;
			}
		}
		return false;
//		return interfaceParamFiles.size() != 0 && interfaceParamFiles != null;
	}

	// this method used to check if there is Excel adapter in json file or not
	private static boolean checkEXCELAdapterPresence(String runTimeId, String name) {
		// TODO Auto-generated method stub
		ArrayNode interfaceParamExcel = HelperFunction.getAdapter(runTimeId, name, "Interface_param_EXCEL");
		for (JsonNode jsonNode : interfaceParamExcel) {
			String value = HelperFunction.searchForParameter(jsonNode, "Name");
			if (!value.equals("\"\"")) {
				return true;
			}
		}
		return false;
//		return interfaceParamExcel.size() != 0 && interfaceParamExcel != null;
	}

	// this method used to check if there is Json adapter in json file or not
	private static boolean checkJsonAdapterPresence(String runTimeId, String name) {
		// TODO Auto-generated method stub
		ArrayNode interfaceParamJson = HelperFunction.getAdapter(runTimeId, name, "Interface_param_JSON");
		for (JsonNode jsonNode : interfaceParamJson) {
			String value = HelperFunction.searchForParameter(jsonNode, "Name").replaceAll("\"", "");
			
			if (!value.equals("")) {
				return true;
			}
		}
		return false;
//		return interfaceParamJson.size() != 0 && interfaceParamJson != null;
	}

	// this method used to check if there is data base adapter in json file or not
	public static boolean checkDataBaseAdapterPresence(String runTimeId, String name) {
		// TODO Auto-generated method stub
		ArrayNode interfaceParamDB = HelperFunction.getAdapter(runTimeId, name, "Interface_param_DB");
		return interfaceParamDB.size() != 0 && interfaceParamDB != null;
	}

}
