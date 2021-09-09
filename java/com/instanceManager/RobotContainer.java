package com.instanceManager;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.entities.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class RobotContainer {
	/* I create bean factory for every type */
	static final BeanFactory beanFactoryForIntegrator = new ClassPathXmlApplicationContext("Integrator-Bean.xml");
	static final BeanFactory beanFactoryForLogical = new ClassPathXmlApplicationContext("Logical-Bean.xml");
	static final BeanFactory beanFactoryForPredictor = new ClassPathXmlApplicationContext("Predictor-Bean.xml");
	static final BeanFactory beanFactoryForDigitalTwin = new ClassPathXmlApplicationContext("Digital-Twin-Bean.xml");
	static final DefaultSingletonBeanRegistry registryForIntegrator = (DefaultSingletonBeanRegistry) ((AbstractRefreshableApplicationContext) beanFactoryForIntegrator)
			.getBeanFactory();
	static final DefaultSingletonBeanRegistry registryForLogical = (DefaultSingletonBeanRegistry) ((AbstractRefreshableApplicationContext) beanFactoryForLogical)
			.getBeanFactory();
	static final DefaultSingletonBeanRegistry registryForPredictor = (DefaultSingletonBeanRegistry) ((AbstractRefreshableApplicationContext) beanFactoryForPredictor)
			.getBeanFactory();
	static final DefaultSingletonBeanRegistry registryForDigitalTwin = (DefaultSingletonBeanRegistry) ((AbstractRefreshableApplicationContext) beanFactoryForDigitalTwin)
			.getBeanFactory();

	static InstanceManager instanceManager = new InstanceManager();
	static IntegratorRobot integratorRobot = new IntegratorRobot();
	static LogicalRobot logicRobot = new LogicalRobot();
	static PredictorRobot predictorRobot = new PredictorRobot();
	static DigitalTwin digitalTwin = new DigitalTwin();

	/*
	 * this method will call from rest api when user want to update instance belongs
	 * to any type of robot or to digitalTwin . in this method we will read the type
	 * of robot and call the method that responsible for update instance of this
	 * type.
	 */
	public static CompletableFuture<String> updateInstance(String runTimeId, String name, ArrayNode fields, String type)
			throws JsonProcessingException, SQLException {
		// TODO Auto-generated method stub
		if (type.equals("IntegratorRobot")) {
			return updateIntegratorInstance(runTimeId, name, fields);
		} else if (type.equals("PredictorRobot")) {
			return updatePredictorInstance(runTimeId, name, fields);
		} else if (type.equals("LogicalRobot")) {
			return updateLogicFlowInstance(runTimeId, name, fields);
		} else {
			return updateDigitalTwinInstance(runTimeId, name, fields);
		}
	}

	public static CompletableFuture<String> updateInstance(String runTimeId, String name, String type, JsonNode json) {
		// TODO Auto-generated method stub
		if (type.equals("IntegratorRobot")) {
			return updateIntegratorInstance(runTimeId, name, json);
		} else if (type.equals("PredictorRobot")) {
			return updatePredictorInstance(runTimeId, name, json);
		} else if (type.equals("LogicalRobot")) {
			return updateLogicFlowInstance(runTimeId, name, json);
		} else {
			return updateDigitalTwinInstance(runTimeId, name, json);
		}
	}

	private static CompletableFuture<String> updateDigitalTwinInstance(String runTimeId, String name, JsonNode json) {
		// TODO Auto-generated method stub
		digitalTwin = (DigitalTwin) beanFactoryForDigitalTwin.getBean(runTimeId);
		/* In order to make sure that the robot with this identity has this name */
		if (!(digitalTwin.getDigitalTwinName().equals(name))) {
			return CompletableFuture.completedFuture("The name you sent does not match the name of the instance");
		}
		digitalTwin.setJson(json);
		return CompletableFuture.completedFuture("the update  process complete correctly");
	}

	private static CompletableFuture<String> updateLogicFlowInstance(String runTimeId, String name, JsonNode json) {
		/* first I will get the Instance that have this run time Id from bean */
		logicRobot = (LogicalRobot) beanFactoryForLogical.getBean(runTimeId);
		/* In order to make sure that the robot with this identity has this name */
		if (!(logicRobot.getRobotName().equals(name))) {
			return CompletableFuture.completedFuture("The name you sent does not match the name of the instance");
		}
		logicRobot.setJson(json);
		return CompletableFuture.completedFuture("the update  process complete correctly");
	}

	private static CompletableFuture<String> updatePredictorInstance(String runTimeId, String name, JsonNode json) {
		// TODO Auto-generated method stub
		predictorRobot = (PredictorRobot) beanFactoryForPredictor.getBean(runTimeId);
		/* In order to make sure that the robot with this identity has this name */
		if (!(predictorRobot.getRobotName().equals(name))) {
			return CompletableFuture.completedFuture("The name you sent does not match the name of the instance");
		}
		predictorRobot.setJson(json);
		/* then I will update transit data values on transit-date table on database */
		return CompletableFuture.completedFuture("the update  process complete correctly");
	}

	private static CompletableFuture<String> updateIntegratorInstance(String runTimeId, String name, JsonNode json) {
		/* first I will get the Instance that have this run time Id from bean */
		integratorRobot = (IntegratorRobot) beanFactoryForIntegrator.getBean(runTimeId);
		/* In order to make sure that the robot with this identity has this name */
		if (!(integratorRobot.getRobotName().equals(name))) {
			return CompletableFuture.completedFuture("The name you sent does not match the name of the instance");
		}

		integratorRobot.setJson(json);
		return CompletableFuture.completedFuture("the update  process complete correctly");
	}

	/*
	 * this method will call from rest api when user want to get Json file in
	 * instance that belongs to any type of robot or to digitalTwin . in this method
	 * we will read the type of robot and call the method that responsible for get
	 * all json file in instance of this type.
	 */

	/*
	 * this method will call from rest api when user want to get some values from
	 * json file from instance that belongs to any type of robot or to digitalTwin .
	 * in this method we will read the type of robot and call the method that
	 * responsible for get values instance of this instance type
	 */
	public static CompletableFuture<List<JsonNode>> getInstance(String runTimeId, String name, ArrayNode fields,
			String type) throws JsonProcessingException, SQLException {
		// TODO Auto-generated method stub
		if (type.equals("IntegratorRobot")) {
			return getValuesFromIntegratorInstance(runTimeId, fields, name);
		} else if (type.equals("PredictorRobot")) {
			return getValuesFromPredictorInstance(runTimeId, fields, name);
		} else if (type.equals("LogicalRobot")) {
			return getValuesFromLogicFlowInstance(runTimeId, fields, name);
		} else {
			return getValuesFromDigitalTwinInstance(runTimeId, fields, name);
		}
	}

	/*
	 * this method are responsible for creating Integrator Instance and call save
	 * method on InstanceManger class to initilize this object
	 */
	static CompletableFuture<IntegratorRobot> createIntegratorInstance(String definitionId, String robotName)
			throws SQLException, JsonProcessingException {
		// TODO Auto-generated method stub
		/* Here I create run time Id and create bean with this Id */
		String runTimeId = createRunTimeIdId();
		// create new integratorRobot bean
		registryForIntegrator.registerSingleton(runTimeId, integratorRobot);
		// set runTimeId attribute in integrator instance
		integratorRobot.setRobotRunTimeId(runTimeId);
		// return new bean
		return CompletableFuture.completedFuture(integratorRobot);

	}

	/* delete integrator instance */
	public static CompletableFuture<String> deleteIntegratorInstance(String runTimeId, String name) {
		try {
			predictorRobot = (PredictorRobot) beanFactoryForPredictor.getBean(runTimeId);
			if (predictorRobot.getRobotName().equals(name)) {
				// first I will destroy bean for this run time Id
				registryForPredictor.destroySingleton(runTimeId);// destroys the bean object
				/* Here I delete this instance from data base */
				if (InstanceManager.deleteInstance(runTimeId)) {
					return CompletableFuture.completedFuture("the delete process work corectlly");
				}
			}
		} catch (JsonProcessingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CompletableFuture.completedFuture("the delete process does not work corectlly");
		}
		return CompletableFuture.completedFuture("the delete process does not work corectlly");
	}

	/*
	 * this method used for update bean instance with this run time Id and for
	 * update those values on database
	 */
	public static CompletableFuture<String> updateIntegratorInstance(String runTimeId, String name, ArrayNode fields) {
		// TODO Auto-generated method stub
		try {
			/* first I will get the Instance that have this run time Id from bean */
			integratorRobot = (IntegratorRobot) beanFactoryForIntegrator.getBean(runTimeId);
			/* In order to make sure that the robot with this identity has this name */
			if (!(integratorRobot.getRobotName().equals(name))) {
				return CompletableFuture.completedFuture("The name you sent does not match the name of the instance");
			}
			/*
			 * then I will update values of them in bean by call updateVariablesValue method
			 * in IntegratorRunTimeManager class.
			 */
			InstanceRunTimeManager.updateValuesOnBeanInstance(integratorRobot, runTimeId, fields);
			/* then I will update transit data values on transit-date table on database */
//			InstanceRunTimeManager.updateVariablesValueOnDB(runTimeId, fields);
			return CompletableFuture.completedFuture("the update  process complete correctly");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CompletableFuture.completedFuture("the process does not complete correctly");
		}
	}

	/* to get some key values of json file that belongs to instance */
	public static CompletableFuture<List<JsonNode>> getValuesFromIntegratorInstance(String runTimeId, ArrayNode fields,
			String name) throws SQLException, JsonProcessingException {
		// TODO Auto-generated method stub
		integratorRobot = (IntegratorRobot) beanFactoryForIntegrator.getBean(runTimeId);
		if (name.equals(integratorRobot.getRobotName())) {
			return CompletableFuture.completedFuture(InstanceRunTimeManager.getValue(integratorRobot, fields));
		}
		return null;
	}

	/* to get all key values of json file that belongs to instance */
	public static CompletableFuture<IntegratorRobot> getValuesFromIntegratorInstance(String runTimeId, String name) {
		// TODO Auto-generated method stub
		integratorRobot = (IntegratorRobot) beanFactoryForIntegrator.getBean(runTimeId);
		if (name.equals(integratorRobot.getRobotName())) {
			return CompletableFuture.completedFuture(integratorRobot);
		}
		return null;
	}

	// this method are responsible for creating LogicFlow Instance
	public static CompletableFuture<LogicalRobot> createLogicFlowInstance(String definitionId, String robotName) {
		// TODO Auto-generated method stub
		/* Here I create run time Id and create bean with this Id */
		String runTimeId = createRunTimeIdId();
		// create new LogicFlow bean
		registryForLogical.registerSingleton(runTimeId, logicRobot);
		// set runTimeId attribute in LogicalRobot instance
		logicRobot.setRobotRunTimeId(runTimeId);
		// return new bean
		return CompletableFuture.completedFuture(logicRobot);
	}

	/* delete logical instance */
	public static CompletableFuture<String> deleteLogicalInstance(String runTimeId, String name)
			throws JsonProcessingException, SQLException {
		// TODO Auto-generated method stub
		logicRobot = (LogicalRobot) beanFactoryForLogical.getBean(runTimeId);
		if (logicRobot.getRobotName().equals(name)) {
			// first I will destroy bean for this run time Id
			registryForLogical.destroySingleton(runTimeId);// destroys the bean object
			/* Here I delete this instance from data base */
			if (InstanceManager.deleteInstance(runTimeId)) {
				return CompletableFuture.completedFuture("the delete process  work corectlly");
			}
		}
		return CompletableFuture.completedFuture("the process does not work corectlly");
	}

	/*
	 * this method used for update bean instance with this run time Id and for
	 * update those values on database
	 */
	private static CompletableFuture<String> updateLogicFlowInstance(String runTimeId, String name, ArrayNode fields) {
		boolean flag;
		try {
			/* first I will get the Instance that have this run time Id from bean */
			logicRobot = (LogicalRobot) beanFactoryForLogical.getBean(runTimeId);
			/* In order to make sure that the robot with this identity has this name */
			if (!(logicRobot.getRobotName().equals(name))) {
				return CompletableFuture.completedFuture("The name you sent does not match the name of the instance");
			}
			/*
			 * then I will update values of them in bean by call updateVariablesValue method
			 * in LogicalRunTimeManager class.
			 */
			InstanceRunTimeManager.updateValuesOnBeanInstance(logicRobot, runTimeId, fields);
			/* then I will update transit data values on transit-date table on database */
			flag = InstanceRunTimeManager.updateVariablesValueOnDB(runTimeId, fields);
			if (flag) {
				return CompletableFuture.completedFuture("the update  process complete correctly");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture("the process does not complete correctly");
	}

	public static CompletableFuture<LogicalRobot> getValuesFromLogicFlowInstance(String runTimeId, String name) {
		// TODO Auto-generated method stub
		logicRobot = (LogicalRobot) beanFactoryForLogical.getBean(runTimeId);
		if (name.equals(logicRobot.getRobotName())) {
			return CompletableFuture.completedFuture(logicRobot);
		}
		return null;
	}

	private static CompletableFuture<List<JsonNode>> getValuesFromLogicFlowInstance(String runTimeId, ArrayNode fields,
			String name) {
		// TODO Auto-generated method stub
		logicRobot = (LogicalRobot) beanFactoryForLogical.getBean(runTimeId);
		if (name.equals(integratorRobot.getRobotName())) {
			try {
				return CompletableFuture.completedFuture(InstanceRunTimeManager.getValue(integratorRobot, fields));
			} catch (JsonProcessingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	// this method are responsible for creating Predictor Instance
	public static CompletableFuture<PredictorRobot> createPredictorInstance(String definitionId, String name) {
		// TODO Auto-generated method stub
		/* Here I create run time Id and create bean with this Id */
		String runTimeId = createRunTimeIdId();
		// create new predictor bean
		registryForPredictor.registerSingleton(runTimeId, predictorRobot);
		// set runTimeId attribute in predictorRobot instance
		predictorRobot.setRobotRunTimeId(runTimeId);
		// return new bean
		return CompletableFuture.completedFuture(predictorRobot);
	}

	/* delete predictor instance */
	public static CompletableFuture<String> deletePredictorInstance(String runTimeId, String name)
			throws JsonProcessingException, SQLException {
		// TODO Auto-generated method stub
		predictorRobot = (PredictorRobot) beanFactoryForPredictor.getBean(runTimeId);
		if (predictorRobot.getRobotName().equals(name)) {
			// first I will destroy bean for this run time Id
			registryForPredictor.destroySingleton(runTimeId);// destroys the bean object
			/* Here I delete this instance from data base */
			if (InstanceManager.deleteInstance(runTimeId)) {
				return CompletableFuture.completedFuture("the delete process work corectlly");
			}
		}
		return CompletableFuture.completedFuture("the delete process does not work corectlly");
	}

	/* update predictor robot instance */
	private static CompletableFuture<String> updatePredictorInstance(String runTimeId, String name, ArrayNode fields) {
		// TODO Auto-generated method stub
		try {
			predictorRobot = (PredictorRobot) beanFactoryForPredictor.getBean(runTimeId);
			/* In order to make sure that the robot with this identity has this name */
			if (!(predictorRobot.getRobotName().equals(name))) {
				return CompletableFuture.completedFuture("The name you sent does not match the name of the instance");
			}
			/*
			 * then I will update values of them in bean by call updateVariablesValue method
			 * in PredictorRunTimeManager class.
			 */
			InstanceRunTimeManager.updateValuesOnBeanInstance(predictorRobot, runTimeId, fields);
			/* then I will update transit data values on transit-date table on database */
			InstanceRunTimeManager.updateVariablesValueOnDB(runTimeId, fields);
			return CompletableFuture.completedFuture("the update  process complete correctly");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CompletableFuture.completedFuture("the process does not complete correctly");
		}
	}

	private static CompletableFuture<List<JsonNode>> getValuesFromPredictorInstance(String runTimeId, ArrayNode fields,
			String name) {
		// TODO Auto-generated method stub
		predictorRobot = (PredictorRobot) beanFactoryForPredictor.getBean(runTimeId);
		if (name.equals(predictorRobot.getRobotName())) {
			try {
				return CompletableFuture.completedFuture(InstanceRunTimeManager.getValue(predictorRobot, fields));
			} catch (JsonProcessingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static CompletableFuture<PredictorRobot> getValuesFromPredictorInstance(String runTimeId, String name) {
		// TODO Auto-generated method stub
		predictorRobot = (PredictorRobot) beanFactoryForPredictor.getBean(runTimeId);
		if (name.equals(predictorRobot.getRobotName())) {
			return CompletableFuture.completedFuture(predictorRobot);
		}
		return null;
	}

	/* create digital twin instance */
	public static CompletableFuture<DigitalTwin> createDigitalTwinInstance(String definitionId, String name) {
		// TODO Auto-generated method stub
		/* Here I create run time Id and create bean with this Id */
		String runTimeId = createRunTimeIdId();
		// create new digital twin bean
		registryForDigitalTwin.registerSingleton(runTimeId, digitalTwin);
		// set runTimeId attribute in digital twin instance
		digitalTwin.setDigitalTwinRuntimeId(runTimeId);
		digitalTwin.setDigitalTwinName(name);

		// return new bean
		return CompletableFuture.completedFuture(digitalTwin);
	}

	/* delete digital twin instance */
	public static CompletableFuture<String> deleteDigitalTwinInstance(String runTimeId, String name)
			throws JsonProcessingException, SQLException {
		digitalTwin = (DigitalTwin) beanFactoryForDigitalTwin.getBean(runTimeId);
		if (digitalTwin.getDigitalTwinName().equals(name)) {
			// first I will destroy bean for this run time Id
			registryForDigitalTwin.destroySingleton(runTimeId);// destroys the bean object
			/* Here I delete this instance from data base */
			if (InstanceManager.deleteInstance(runTimeId)) {
				return CompletableFuture.completedFuture("the delete process work corectlly");
			}
		}
		return CompletableFuture.completedFuture("the process does not work corectlly");
	}

	/* update digital twin instance */
	private static CompletableFuture<String> updateDigitalTwinInstance(String runTimeId, String name,
			ArrayNode fields) {
		// TODO Auto-generated method stub
		try {
			digitalTwin = (DigitalTwin) beanFactoryForDigitalTwin.getBean(runTimeId);
			/* In order to make sure that the robot with this identity has this name */
			if (!(digitalTwin.getDigitalTwinName().equals(name))) {
				return CompletableFuture.completedFuture("The name you sent does not match the name of the instance");
			}
			/*
			 * then I will update values of them in bean by call updateVariablesValue method
			 * in DigitalTwinRunTimeManager class.
			 */
			/* then I will update transit data values on transit-date table on database */
			InstanceRunTimeManager.updateValuesOnBeanInstance(digitalTwin, runTimeId, fields);
			boolean flag = InstanceRunTimeManager.updateVariablesValueOnDB(runTimeId, fields);
			if (flag) {
				return CompletableFuture.completedFuture("the update  process complete correctly");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture("the process does not complete correctly");
	}

	public static CompletableFuture<JsonNode> getValuesFromDigitalTwinInstance(String runTimeId, String name) {
		// TODO Auto-generated method stub
		digitalTwin = (DigitalTwin) beanFactoryForDigitalTwin.getBean(runTimeId);
		if (name.equals(digitalTwin.getDigitalTwinName())) {
			return CompletableFuture.completedFuture(digitalTwin.getJson());
		}
		return null;
	}

	private static CompletableFuture<List<JsonNode>> getValuesFromDigitalTwinInstance(String runTimeId,
			ArrayNode fields, String name) {
		// TODO Auto-generated method stub
		digitalTwin = (DigitalTwin) beanFactoryForDigitalTwin.getBean(runTimeId);
		if (name.equals(digitalTwin.getDigitalTwinName())) {
			try {
				return CompletableFuture.completedFuture(InstanceRunTimeManager.getValue(digitalTwin, fields));
			} catch (JsonProcessingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	// to get digital twin instance
	public static CompletableFuture<DigitalTwin> getDigitalTwinInstance(String runTimeId, String name) {
		// TODO Auto-generated method stub
		digitalTwin = (DigitalTwin) beanFactoryForDigitalTwin.getBean(runTimeId);
		if (name.equals(digitalTwin.getDigitalTwinName())) {
			return CompletableFuture.completedFuture(digitalTwin);
		}
		return null;
	}

	/* this method used to create run time id */
	public static String createRunTimeIdId() {
		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString();
		return uuidAsString;
	}

}