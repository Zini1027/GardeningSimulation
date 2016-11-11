package sensor;

import controller.OnOffController;
import main.Constants;
import main.Environment;

public class WaterContentSensor extends Sensor {

	private OnOffController sprinkler;
	private OnOffController roof;
	
	public WaterContentSensor(OnOffController sprinkler, OnOffController roof) {
		super();
		name = "WaterContent";
		lastRead = Constants.INIT_WATER_CONTENT;
		this.sprinkler = sprinkler;
		this.roof = roof;
	}

	@Override
	public float read(float time) {
		float envPrecipitation = Environment.getPrecipitation(lastTime);
		float currentRead = lastRead;
		float deltaT = time - lastTime;
		if (envPrecipitation > 0 && roof.isOn()) {
			currentRead += envPrecipitation * deltaT * Constants.WATER_CONTENT_PER_PREC;
		}
		if (sprinkler.isOn()) {
			currentRead += Constants.WATER_CONTENT_SPRINKER_RATE * deltaT * Constants.WATER_CONTENT_PER_PREC;
		}
		currentRead += Constants.WATER_CONTENT_EVAPORATION_RATE * deltaT * Constants.WATER_CONTENT_PER_PREC;
		
		lastTime = time;
		lastRead = currentRead;
		return currentRead;
	}

}
