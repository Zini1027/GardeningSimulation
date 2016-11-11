package controller;

import main.AutoGardeningSystem;

public class SupplyController extends Controller {

	public SupplyController(String name) {
		super(name);
	}

	public void supply(float amount) {
		if (amount > 0.00001) {
			logger.info("%s: Supply %f gram of \"%s\" to GreenHouseUnit \"%s\"",
					AutoGardeningSystem.getInstance().getCurrentTimeString(), amount, name,
					greenHouseUnit.getGrowingPlan().getPlantName());
		}
	}

}
