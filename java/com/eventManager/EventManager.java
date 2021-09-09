package com.eventManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import com.dataBaseManager.DataBaseFunctions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jsonOperation.JsonOperation;
import com.runTimeController.HelperFunction;

public class EventManager extends DataBaseFunctions {
	static Timer timer = new Timer();

	/*
	 * this method go to digital twin table and get all digital twins that have time
	 * event type , then call creatDigitalTwin method to schedule creation of this
	 * digital twin in event time .
	 */
	public static void excuteTimeEvent() throws SQLException, JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		PreparedStatement ps = getPreparedStatement(
				"SELECT digital_twin_id, digital_twin_name, json FROM digital_twin WHERE type = ?");
		ps.setString(1, "\"time\"");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			JsonNode json = JsonOperation.convertStringToJsonNode(rs.getString("json"));
			String digitalTwinDefinitionId = rs.getString("digital_twin_id");
			String digitalTwinName = rs.getString("digital_twin_name");
			/* deal with event in specfic date and event in specfic day */
			ArrayNode event_by_date = HelperFunction.getArrayNode(json, "by_date");
			JsonNode event_by_day = HelperFunction.searchForKeyValue(json, "by_day");
			for (JsonNode dateEvent : event_by_date) {
				String event_date = HelperFunction.searchForString(dateEvent, "event_date");
				String event_time = HelperFunction.searchForString(dateEvent, "event_time");
				if (!(event_date.equals("\"\"")) && !(event_time.equals("\"\""))) {
					Calendar date = generateDate(event_date, event_time);
					creatDigitalTwin(date, digitalTwinDefinitionId, digitalTwinName, 1);
				}
			}
			String event_day = HelperFunction.searchForString(event_by_day, "event_day");
			String event_time = HelperFunction.searchForString(event_by_day, "event_time");
			String frequency = HelperFunction.searchForString(event_by_day, "event_frequency");
			frequency = frequency.replaceAll("\"", "");

			if (!event_day.equals("\"\"")) {
				Calendar day = generateDay(event_day, event_time);
				creatDigitalTwin(day, digitalTwinDefinitionId, digitalTwinName, Integer.parseInt(frequency));
			}
		}
	}

	/* this method used to set String date in Calender object */
	private static Calendar generateDate(String event_date, String event_time) {
		Calendar calender = Calendar.getInstance();
		event_date = event_date.replaceAll("\"", "");
		event_time = event_time.replaceAll("\"", "");
		// TODO Auto-generated method stub
		String[] arr = event_date.split("/");
		int[] date = new int[arr.length];
		for (int i = 0; i < date.length; i++) {
			date[i] = Integer.parseInt(arr[i]);
		}
		int[] hour = generateHour(event_time);
		calender.set(Calendar.YEAR, date[2]);
		switch (arr[1]) {
		case "1":
			calender.set(Calendar.MONTH, Calendar.JANUARY);
			break;
		case "2":
			calender.set(Calendar.MONTH, Calendar.FEBRUARY);
			break;
		case "3":
			calender.set(Calendar.MONTH, Calendar.MARCH);
			break;
		case "4":
			calender.set(Calendar.MONTH, Calendar.APRIL);
			break;
		case "5":
			calender.set(Calendar.MONTH, Calendar.MAY);
			break;
		case "6":
			calender.set(Calendar.MONTH, Calendar.JUNE);
			break;
		case "7":
			calender.set(Calendar.MONTH, Calendar.JULY);
			break;
		case "8":
			calender.set(Calendar.MONTH, Calendar.AUGUST);
			break;
		case "9":
			calender.set(Calendar.MONTH, Calendar.SEPTEMBER);
			break;
		case "10":
			calender.set(Calendar.MONTH, Calendar.OCTOBER);
			break;
		case "11":
			calender.set(Calendar.MONTH, Calendar.NOVEMBER);
			break;
		case "12":
			calender.set(Calendar.MONTH, Calendar.DECEMBER);
			break;
		}
		calender.set(Calendar.MONTH, Calendar.AUGUST);
		calender.set(Calendar.DAY_OF_MONTH, date[0]);
		calender.set(Calendar.HOUR_OF_DAY, hour[0]);
		calender.set(Calendar.MINUTE, hour[1]);
		calender.set(Calendar.SECOND, 0);
		return calender;
	}

	/* this method used to set String day and hour in Calender object */
	private static Calendar generateDay(String event_day, String event_time) {
		Calendar calender = Calendar.getInstance();
		// TODO Auto-generated method stub
		event_time = event_time.replaceAll("\"", "");
		int[] hour = generateHour(event_time);
		event_day = event_day.toLowerCase();
		switch (event_day) {
		case "saturday":
			calender.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			break;
		case "sunday":
			calender.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			break;
		case "monday":
			calender.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			break;
		case "tuesday":
			calender.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			break;
		case "wednesday":
			calender.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
			break;
		case "thursday":
			calender.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
			break;
		case "friday":
			calender.set(Calendar.DAY_OF_WEEK, Calendar.FEBRUARY);
			break;
		}
		calender.set(Calendar.HOUR_OF_DAY, hour[0]);
		calender.set(Calendar.MINUTE, hour[1]);
		calender.set(Calendar.SECOND, 0);
		return calender;
	}

	/*
	 * this method used to conver hour format in string to array of integars : first
	 * element in array is hours number , second element in array is minute number .
	 */
	private static int[] generateHour(String event_time) {
		// TODO Auto-generated method stub
		String[] arr = event_time.split(":");
		int[] hour = new int[arr.length];
		for (int i = 0; i < hour.length; i++) {
			if (arr[i].charAt(0) == '0') {
				hour[i] = Character.getNumericValue(arr[i].charAt(1));
			} else {
				hour[i] = Integer.parseInt(arr[i]);
			}
		}
		return hour;
	}

	/* used to schedule time to "create digital twin instance on it */
	public static void creatDigitalTwin(Calendar time, String definitionId, String digitalTwinName, int frequency) {
		// TODO Auto-generated method stub
		System.out.println(time.getTime());
		if (!time.getTime().before(new Date())) {
			timer.schedule(new FrequentTimeTask(definitionId, digitalTwinName, frequency), time.getTime());
		}
	}

//	private static void creatDigitalTwinn(Calendar time, String digitalTwinDefinitionId, String digitalTwinName,
//			int frequency) {
//		// TODO Auto-generated method stub
//		int dif=0;
//		do {
//			if (!time.getTime().before(new Date())) {
//				System.out.println(time.getTime());
//				time.set(Calendar.DATE, time.getTime().getDate()+dif);
//				TimeTrigger timeTrigger = new TimeTrigger(digitalTwinDefinitionId, digitalTwinName);
//				timer.schedule(timeTrigger, time.getTime());
//				dif=7;
//				frequency--;
//			}
//		} while (frequency > 1);
//	}
}