package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.GreenHouseUnit;

public abstract class Controller {
	protected static final Logger logger = LogManager.getFormatterLogger();
	protected String name;
	protected GreenHouseUnit greenHouseUnit;

	public Controller(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public GreenHouseUnit getGreenHouseUnit() {
		return greenHouseUnit;
	}

	public void setGreenHouseUnit(GreenHouseUnit greenHouseUnit) {
		this.greenHouseUnit = greenHouseUnit;
	}

	public void setName(String name) {
		this.name = name;
	}
}
