package com.entities;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
@Scope("prototype")
public class Robot implements BeanNameAware {
	private String robotDefinitionId;
	private String robotRunTimeId;
	private String robotName;
	private String digitalTwinDefinitionId;
	private JsonNode json;
	public String BeanName;

	public String getRobotDefinitionId() {
		return robotDefinitionId;
	}

	public void setRobotDefinitionId(String robotDefinitionId) {
		this.robotDefinitionId = robotDefinitionId;
	}

	public JsonNode getJson() {
		return json;
	}

	public void setJson(JsonNode json) {
		this.json = json;
	}

	public String getRobotRunTimeId() {
		return robotRunTimeId;
	}

	public void setRobotRunTimeId(String robotRunTimeId) {
		this.robotRunTimeId = robotRunTimeId;
	}

	public String getRobotName() {
		return robotName;
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public String getDigitalTwinDefinitionId() {
		return digitalTwinDefinitionId;
	}

	public void setDigitalTwinDefinitionId(String digitalTwinDefinitionId) {
		this.digitalTwinDefinitionId = digitalTwinDefinitionId;
	}

	@Override
	public void setBeanName(String name) {
		// TODO Auto-generated method stub
		System.out.println(name);
		System.out.println(BeanName);
		this.BeanName = "123";
		System.out.println(BeanName);

	}

}
