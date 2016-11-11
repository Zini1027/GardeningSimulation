package sensor;

import controller.OnOffController;
import main.Constants;
import main.Environment;

public class HumiditySensor extends Sensor {

	private OnOffController humidifier;
	private OnOffController fan;
	
	public HumiditySensor(OnOffController humidifier, OnOffController fan) {
		super();
		name = "Humidity";
		lastRead = Environment.getHumidity(0);
		this.humidifier = humidifier;
		this.fan = fan;
	}

	@Override
	public float read(float time) {
		float envHumidity = Environment.getHumidity(lastTime);
		float currentRead = lastRead;
		float deltaT = time - lastTime;
		if (envHumidity > lastRead) {
			currentRead += Constants.HUMIDITY_NATURAL_EXCHANGE_RATE * deltaT; 
		} else if (envHumidity < lastRead) {
			currentRead -= Constants.HUMIDITY_NATURAL_EXCHANGE_RATE * deltaT;
		}
		if (humidifier.isOn()) {
			currentRead += Constants.HUMIDITY_HUMIDIFIER_RATE * deltaT;
		}
		if (fan.isOn()) {
			currentRead += Constants.HUMIDITY_FAN_RATE * deltaT;
		}
		
		lastTime = time;
		lastRead = currentRead;
		return currentRead;
	}

}
