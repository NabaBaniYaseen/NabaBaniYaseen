package com.entities;

import java.util.Map;

public class Event {
	private String eventId;
	private String eventName;
	private String eventRunTimeId;
	private Map<String, Object> properties;
	private String stringProperties;

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public String getStringProperties() {
		return stringProperties;
	}

	public void setStringProperties(String stringProperties) {
		this.stringProperties = stringProperties;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventRunTimeId() {
		return eventRunTimeId;
	}

	public void setEventRunTimeId(String eventRunTimeId) {
		this.eventRunTimeId = eventRunTimeId;
	}
}
