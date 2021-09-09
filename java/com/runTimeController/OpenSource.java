package com.runTimeController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OpenSource {
	static BufferedReader reader;
	static String line;
	static StringBuffer responseContent = new StringBuffer();
	static StringBuffer openSourceEquationBuffer = new StringBuffer();

	public static String callOpenSource(String openSourceEquation, ArrayList<String> input, int col, int row,
			int sheet) {
		// first I will add open source method to input list
		input.add(openSourceEquation);
		// add ' to open source equation and convert it to string (change format)
		for (int i = 0; i < input.size(); i++) {
			if (i != input.size() - 1) {
				openSourceEquationBuffer.append("'" + input.get(i) + "',");
			} else {
				openSourceEquationBuffer.append("'" + input.get(i) + "'");
			}
		}
		try {
			// define request
			URL url = new URL(
					"http://localhost:800/calculate/" + openSourceEquationBuffer + '/' + col + '/' + row + '/' + sheet);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// set connection configuration
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			int status = connection.getResponseCode();
			// get respone
			if (status > 299) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			}
			return responseContent.toString();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;

		}
	}

	//I add main method for test
	public static void main(String[] args) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("10");
		arr.add("20");
		arr.add("30");
	
		System.out.println(callOpenSource("=SUM(A1:C1)", arr, 3, 0, 0));
	}

}
