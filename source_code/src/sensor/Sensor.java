package sensor;

public abstract class Sensor {
	protected String name;
	protected float lastTime = 0;
	protected float lastRead;
	
	public String getName() {
		return name;
	}
	public abstract float read(float time);
}
