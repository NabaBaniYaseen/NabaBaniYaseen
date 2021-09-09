package com.eventManager;

import java.sql.SQLException;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.script.ScriptException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sequencer.Sequencer;

/* this class used to call createDigitalTwinInstance method from Sequencer in specfic date*/
public class TimeTrigger extends TimerTask {
	private String definitionId;
	private String digitalTwinName;
	/*
	 * we need digitalTwinName && definitionId when we want to create digital Twin
	 * Instane so we recieve these values from constructor
	 */

	public TimeTrigger(String definitionId, String digitalTwinName) {
		super();
		this.definitionId = definitionId;
		this.digitalTwinName = digitalTwinName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			/*
			 * In the time event, we do not have a file that contains parameters and return
			 * value like other types of events so we pass null value
			 */
			Sequencer.createDigitalTwinInstance(definitionId, digitalTwinName, "Time", null);
		} catch (JsonProcessingException | InterruptedException | ExecutionException | SQLException
				| ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}