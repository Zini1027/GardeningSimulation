package controller;

import main.AutoGardeningSystem;

public class OnOffController extends Controller {
	private boolean isOn = false;

	public OnOffController(String name) {
		super(name);
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		if (this.isOn != isOn) {
			logger.info("%s: Turn %s \"%s\" for GreenHouseUnit \"%s\"",
					AutoGardeningSystem.getInstance().getCurrentTimeString(), isOn ? "on" : "off", name,
					greenHouseUnit.getGrowingPlan().getPlantName());
		} else {
			logger.debug("%s: \"%s\" is currently %s for GreenHouseUnit \"%s\"",
					AutoGardeningSystem.getInstance().getCurrentTimeString(),name, isOn ? "on" : "off",
					greenHouseUnit.getGrowingPlan().getPlantName());
		}
		this.isOn = isOn;
	}
}