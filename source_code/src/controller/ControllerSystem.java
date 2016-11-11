package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.AutoGardeningSystem;
import main.Constants;
import main.Environment;
import main.GreenHouseUnit;

public class ControllerSystem {

	private static final Logger logger = LogManager.getFormatterLogger();
	private GreenHouseUnit greenHouseUnit;
	private OnOffController heater = new OnOffController("heater");
	private OnOffController ac = new OnOffController("ac");
	private OnOffController humidifier = new OnOffController("humidifier");
	private OnOffController fan = new OnOffController("fan");
	private PercentageController shades = new PercentageController("shades");
	private PercentageController lightBlobs = new PercentageController("lightBlobs");
	private OnOffController sprinkler = new OnOffController("sprinkler");
	private OnOffController roof = new OnOffController("roof");
	private SupplyController pesticide = new SupplyController("pesticide");
	private SupplyController nitrogen = new SupplyController("nitrogen");

	public ControllerSystem(GreenHouseUnit greenHouseUnit) {
		this.greenHouseUnit = greenHouseUnit;
		heater.setGreenHouseUnit(greenHouseUnit);
		ac.setGreenHouseUnit(greenHouseUnit);
		humidifier.setGreenHouseUnit(greenHouseUnit);
		fan.setGreenHouseUnit(greenHouseUnit);
		shades.setGreenHouseUnit(greenHouseUnit);
		lightBlobs.setGreenHouseUnit(greenHouseUnit);
		sprinkler.setGreenHouseUnit(greenHouseUnit);
		roof.setGreenHouseUnit(greenHouseUnit);
		pesticide.setGreenHouseUnit(greenHouseUnit);
		nitrogen.setGreenHouseUnit(greenHouseUnit);
	}

	public void adjustTemperature(float time, float current, float target) {
		logger.info(
				"%s: in GreenHouseUnit \"%s\" outdoor environment temperature: %f indoor sensor reading: %f plant requirement: %f",
				AutoGardeningSystem.getInstance().getCurrentTimeString(),
				greenHouseUnit.getGrowingPlan().getPlantName(), Environment.getTemperature(time), current, target);
		if (!withinErrorBuffer(current, target)) {
			if (current > target) {
				ac.setOn(true);
				heater.setOn(false);
			} else {
				ac.setOn(false);
				heater.setOn(true);
			}
		} else {
			ac.setOn(false);
			heater.setOn(false);
		}
	}

	public void adjustHumidity(float time, float current, float target) {
		logger.info(
				"%s: in GreenHouseUnit \"%s\" outdoor environment humidity: %f indoor sensor reading: %f plant requirement: %f",
				AutoGardeningSystem.getInstance().getCurrentTimeString(),
				greenHouseUnit.getGrowingPlan().getPlantName(), Environment.getHumidity(time), current, target);
		if (!withinErrorBuffer(current, target)) {
			if (current > target) {
				fan.setOn(true);
				humidifier.setOn(false);
			} else {
				fan.setOn(false);
				humidifier.setOn(true);
			}
		} else {
			fan.setOn(false);
			humidifier.setOn(false);
		}
	}

	public void adjustLight(float time, float current, float target) {
		logger.info(
				"%s: in GreenHouseUnit \"%s\" outdoor sun light: %f indoor light sensor reading: %f plant requirement: %f",
				AutoGardeningSystem.getInstance().getCurrentTimeString(),
				greenHouseUnit.getGrowingPlan().getPlantName(), Environment.getLight(time), current, target);
		if (!withinErrorBuffer(current, target)) {
			float envLight = Environment.getLight(time);
			if (envLight >= target) {
				// turn off light blobs, adjust shades
				shades.setPercentage(1 - target / envLight);
				lightBlobs.setPercentage(0);
			} else {
				// turn off shades, turn on light blobs
				shades.setPercentage(0);
				if (target - envLight > Constants.MAX_LUMIN_FROM_LIGHT_BLOBS) {
					lightBlobs.setPercentage(1);
					logger.warn("Cannot provide enough light.");
				} else {
					lightBlobs.setPercentage((target - envLight) / Constants.MAX_LUMIN_FROM_LIGHT_BLOBS);
				}
			}
		}
	}

	public void adjustWaterContent(float time, float current, float target) {
		logger.info(
				"%s: in GreenHouseUnit \"%s\" outdoor precipitation: %f indoor waterContent sensor reading: %f plant requirement: %f",
				AutoGardeningSystem.getInstance().getCurrentTimeString(),
				greenHouseUnit.getGrowingPlan().getPlantName(), Environment.getPrecipitation(time), current, target);
		if (!withinErrorBuffer(current, target)) {
			if (current > target) {
				roof.setOn(false);
				sprinkler.setOn(false);
			} else {
				if (Environment.getPrecipitation(time) > 0) {
					roof.setOn(true);
					sprinkler.setOn(false);
				} else {
					sprinkler.setOn(true);
				}
			}
		} else {
			sprinkler.setOn(false);
		}
	}

	public void supplyNitrogen(float time, float amount) {
		nitrogen.supply(amount);
	}

	public void supplyPesticide(float time, float pestLevel) {
		pesticide.supply(pestLevel * Constants.PEST_PESTICIDE_PER_PEST_LEVEL);
	}

	public OnOffController getHeater() {
		return heater;
	}

	public OnOffController getAc() {
		return ac;
	}

	public OnOffController getHumidifier() {
		return humidifier;
	}

	public OnOffController getFan() {
		return fan;
	}

	public PercentageController getShades() {
		return shades;
	}

	public PercentageController getLightBlobs() {
		return lightBlobs;
	}

	public OnOffController getSprinkler() {
		return sprinkler;
	}

	public OnOffController getRoof() {
		return roof;
	}

	public SupplyController getPesticide() {
		return pesticide;
	}

	public SupplyController getNitrogen() {
		return nitrogen;
	}

	private static boolean withinErrorBuffer(float current, float target) {
		return Math.abs(current - target) / target * 100 < Constants.METRIC_ERROR_BUFFER_PERCENTAGE;
	}
}
