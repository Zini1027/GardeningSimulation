package sensor;

import controller.PercentageController;
import main.Constants;
import main.Environment;

public class LightSensor extends Sensor {

	private PercentageController shades;
	private PercentageController lightBlobs;

	public LightSensor(PercentageController shades, PercentageController lightBlobs) {
		super();
		name = "Light";
		lastRead = Environment.getLight(0);
		this.shades = shades;
		this.lightBlobs = lightBlobs;
	}

	@Override
	public float read(float time) {

		return Environment.getLight(time) * (1 - shades.getPercentage())
				+ lightBlobs.getPercentage() * Constants.MAX_LUMIN_FROM_LIGHT_BLOBS;

	}

}
