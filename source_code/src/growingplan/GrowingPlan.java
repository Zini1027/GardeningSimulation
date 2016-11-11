package growingplan;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GrowingPlan {
	private static final Logger logger = LogManager.getFormatterLogger();
	private String plantName;
	private WholeLifeSpec temperaturePlan;
	private WholeLifeSpec humidityPlan;
	private WholeLifeSpec lightPlan;
	private WholeLifeSpec waterContentPlan;
	private WholeLifeSpec nitrogenPlan;

	public GrowingPlan(String plantName, WholeLifeSpec temperaturePlan, WholeLifeSpec humidityPlan,
			WholeLifeSpec lightPlan, WholeLifeSpec waterContentPlan, WholeLifeSpec nitrogenPlan) {
		this.plantName = plantName;
		this.temperaturePlan = temperaturePlan;
		this.humidityPlan = humidityPlan;
		this.lightPlan = lightPlan;
		this.waterContentPlan = waterContentPlan;
		this.nitrogenPlan = nitrogenPlan;
		logger.info(toString());
	}

	@Override
	public String toString() {
		return String.format("GrowingPlan for \"%s\" is:\nTemperature:\n%sHumidity:\n%sLight:\n%sWaterContent:\n%sNitrogen:\n%s",
				plantName, temperaturePlan, humidityPlan, lightPlan, waterContentPlan, nitrogenPlan);
	}
	
	public GrowingPlan(String plantName, String temperaturePlan, String humidityPlan, String lightPlan,
			String waterContentPlan, String nitrogenPlan) {
		this(plantName, WholeLifeSpec.parse(temperaturePlan), WholeLifeSpec.parse(humidityPlan),
				WholeLifeSpec.parse(lightPlan), WholeLifeSpec.parse(waterContentPlan),
				WholeLifeSpec.parse(nitrogenPlan));
	}

	public String getPlantName() {
		return plantName;
	}

	public float getTemperature(float time) {
		return temperaturePlan.getValue(time);
	}

	public float getHumidity(float time) {
		return humidityPlan.getValue(time);
	}

	public float getLight(float time) {
		return lightPlan.getValue(time);
	}

	public float getWaterContent(float time) {
		return waterContentPlan.getValue(time);
	}

	public float getNitrogen(float time) {
		return nitrogenPlan.getValue(time);
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public void setTemperaturePlan(WholeLifeSpec temperaturePlan) {
		this.temperaturePlan = temperaturePlan;
	}

	public void setHumidityPlan(WholeLifeSpec humidityPlan) {
		this.humidityPlan = humidityPlan;
	}

	public void setLightPlan(WholeLifeSpec lightPlan) {
		this.lightPlan = lightPlan;
	}

	public void setWaterContentPlan(WholeLifeSpec waterContentPlan) {
		this.waterContentPlan = waterContentPlan;
	}

	public void setNitrogenPlan(WholeLifeSpec nitrogenPlan) {
		this.nitrogenPlan = nitrogenPlan;
	}

}
