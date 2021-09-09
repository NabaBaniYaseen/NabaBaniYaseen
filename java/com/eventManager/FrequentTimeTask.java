package com.eventManager;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.script.ScriptException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sequencer.Sequencer;

public class FrequentTimeTask extends TimerTask {
	private Timer timer = new Timer();
	private String definitionId;
	private String digitalTwinName;
	private int frequency;
	/*
	 * we need digitalTwinName && definitionId when we want to create digital Twin
	 * Instane so we recieve these values from constructor
	 */

	public FrequentTimeTask(String definitionId, String digitalTwinName, int frequency) {
		super();
		this.definitionId = definitionId;
		this.digitalTwinName = digitalTwinName;
		this.frequency = frequency;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			/*
			 * In the time event, we do not have a file that contains parameters and return
			 * value like other types of events so we pass null value
			 */
			Sequencer.createDigitalTwinInstance(definitionId, digitalTwinName, "Time", null);
			if (frequency > 1) {
				Date date = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DATE, date.getDate() + 7);
				timer.schedule(new FrequentTimeTask(definitionId, digitalTwinName, frequency - 1), calendar.getTime());
				System.out.println(calendar.getTime());
			}
		} catch (JsonProcessingException | InterruptedException | ExecutionException | SQLException
				| ScriptException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
