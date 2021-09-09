package com.runTimeController;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class AIExecution {

	public static void callApi(String aPIName, String apiUrl, ArrayNode parameters) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL(apiUrl);
			Map<String, String> mapParameters = addRequestParameters(parameters);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			Iterator<Map.Entry<String, String>> itr = mapParameters.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, String> entry = itr.next();
				con.setRequestProperty(entry.getKey(), entry.getValue());
			}
			con.setRequestMethod("GET");
			// set request paramaeter
			con.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(getParamsString(mapParameters));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Map<String, String> addRequestParameters(ArrayNode parameters) {
		// TODO Auto-generated method stub
		Map<String, String> parametersMap = new HashMap<>();
		for (JsonNode param : parameters) {
			Iterator<Entry<String, JsonNode>> iterator = param.fields();
			while (iterator.hasNext()) {
				Entry<String, JsonNode> entry = iterator.next();
				String key = entry.getKey();
				String value = HelperFunction.convertJsonNodeToString(entry.getValue());
				parametersMap.put(key, value);
			}
		}

		return parametersMap;
	}

	public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}

}
