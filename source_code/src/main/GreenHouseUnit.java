package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.ControllerSystem;
import growingplan.GrowingPlan;
import sensor.HumiditySensor;
import sensor.LightSensor;
import sensor.PestLevelSensor;
import sensor.Sensor;
import sensor.TemperatureSensor;
import sensor.WaterContentSensor;

public class GreenHouseUnit {
	private static final Logger logger = LogManager.getFormatterLogger();
	private GrowingPlan growingPlan;
	private ControllerSystem controllerSystem;

	private Sensor temperatureSensor;
	private Sensor humiditySensor;
	private Sensor lightSensor;
	private Sensor pestLevelSensor;
	private Sensor waterContentSensor;

	public GreenHouseUnit(GrowingPlan growingPlan) {
		
		this.growingPlan = growingPlan;
		controllerSystem = new ControllerSystem(this);
		temperatureSensor = new TemperatureSensor(controllerSystem.getHeater(), controllerSystem.getAc());
		humiditySensor = new HumiditySensor(controllerSystem.getHumidifier(), controllerSystem.getFan());
		lightSensor = new LightSensor(controllerSystem.getShades(), controllerSystem.getLightBlobs());
		pestLevelSensor = new PestLevelSensor();
		waterContentSensor = new WaterContentSensor(controllerSystem.getSprinkler(), controllerSystem.getRoof());
	}

	public void update(float time) {

		controllerSystem.adjustTemperature(time, temperatureSensor.read(time), growingPlan.getTemperature(time));
		controllerSystem.adjustHumidity(time, humiditySensor.read(time), growingPlan.getHumidity(time));
		controllerSystem.adjustLight(time, lightSensor.read(time), growingPlan.getLight(time));
		controllerSystem.adjustWaterContent(time, waterContentSensor.read(time), growingPlan.getWaterContent(time));
		controllerSystem.supplyNitrogen(time, growingPlan.getNitrogen(time));
		controllerSystem.supplyPesticide(time, pestLevelSensor.read(time));
	}

	public GrowingPlan getGrowingPlan() {
		return growingPlan;
	}
}
