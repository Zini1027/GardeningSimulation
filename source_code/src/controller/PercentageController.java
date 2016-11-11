package controller;

import main.AutoGardeningSystem;

/**
 * Operation granularity: 10%
 *
 */
public class PercentageController extends Controller {

	/**
	 * value: 0~1
	 */
	private float percentage = 0;

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		
		float old = this.percentage;
		this.percentage = ((float) Math.round(percentage * 10)) / 10;
		
		if (this.percentage != old) {
			logger.info("%s: Set \"%s\" to %d%% for GreenHouseUnit \"%s\"",
					AutoGardeningSystem.getInstance().getCurrentTimeString(), name, (int)(this.percentage*100),
					greenHouseUnit.getGrowingPlan().getPlantName());
		} else {
			logger.debug("%s: \"%s\" is currently set to %d%% for GreenHouseUnit \"%s\"",
					AutoGardeningSystem.getInstance().getCurrentTimeString(), name, (int)(this.percentage*100),
					greenHouseUnit.getGrowingPlan().getPlantName());
		}
	}

	public PercentageController(String name) {
		super(name);
	}

}
