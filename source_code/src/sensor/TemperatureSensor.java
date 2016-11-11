package sensor;

import controller.OnOffController;
import main.Constants;
import main.Environment;

public class TemperatureSensor extends Sensor {

	private OnOffController heater;
	private OnOffController ac;
	
	public TemperatureSensor(OnOffController heater, OnOffController ac) {
		super();
		name = "Temperature";
		lastRead = Environment.getTemperature(0);
		this.heater = heater;
		this.ac = ac;
	}

	@Override
	public float read(float time) {
		float envTemperature = Environment.getTemperature(lastTime);
		float currentRead = lastRead;
		float deltaT = time - lastTime;
		if (envTemperature > lastRead) {
			currentRead += Constants.TEMP_NATURAL_EXCHANGE_RATE * deltaT; 
		} else if (envTemperature < lastRead) {
			currentRead -= Constants.TEMP_NATURAL_EXCHANGE_RATE * deltaT;
		}
		if (heater.isOn()) {
			currentRead += Constants.TEMP_HEATER_RATE * deltaT;
		}
		if (ac.isOn()) {
			currentRead += Constants.TEMP_AC_RATE * deltaT;
		}
		
		lastTime = time;
		lastRead = currentRead;
		return currentRead;
	}

}
