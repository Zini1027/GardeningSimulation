package sensor;

import main.Environment;

public class PestLevelSensor extends Sensor {

	public PestLevelSensor() {
		name = "PestLevel";
		lastRead = Environment.getPestLevel(0);
	}
	
	@Override
	public float read(float time) {
		return Environment.getPestLevel(time);
	}

}
