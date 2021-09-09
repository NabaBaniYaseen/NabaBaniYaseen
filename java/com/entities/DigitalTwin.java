package com.entities;

import com.fasterxml.jackson.databind.JsonNode;

public class DigitalTwin {
	private String digitalTwinId;
	private String digitalTwinRuntimeId;
	private String digitalTwinName;
	private Systems system;
	private JsonNode json;

	public String getDigitalTwinId() {
		return digitalTwinId;
	}

	public JsonNode getJson() {
		return json;
	}

	public void setJson(JsonNode json) {
		this.json = json;
	}

	public void setDigitalTwinId(String digitalTwinId) {
		this.digitalTwinId = digitalTwinId;
	}

	public String getDigitalTwinRuntimeId() {
		return digitalTwinRuntimeId;
	}

	public void setDigitalTwinRuntimeId(String digitalTwinRuntimeId) {
		this.digitalTwinRuntimeId = digitalTwinRuntimeId;
	}

	public String getDigitalTwinName() {
		return digitalTwinName;
	}

	public void setDigitalTwinName(String digitalTwinName) {
		this.digitalTwinName = digitalTwinName;
	}

	public Systems getSystem() {
		return system;
	}

	public void setSystem(Systems system) {
		this.system = system;
	}

}
