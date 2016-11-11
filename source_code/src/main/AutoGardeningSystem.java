package main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import growingplan.GrowingPlan;
import ui.UIRunner;

public class AutoGardeningSystem {
	private static AutoGardeningSystem singleton;
	private int maxDays;
	private float speed;
	private static final Logger logger = LogManager.getFormatterLogger();

	public static void main(String[] args) {
		// command line flags
		Options options = new Options();
		options.addOption("d", true, "[integer] Number of days to simulate, the days are in the gardening world. MINIMUM value:45, DEFAULT:60");
		options.addOption("s", true, "[float] Speed of simulation, the number of real world seconds is equal to one hour in the gardening world. "
				+ "For example s = 5 means 5 second in real world == 1 hour in gardening world. DEFAULT: 1");
		options.addOption("h", false, "Print help message");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse( options, args);
		} catch (ParseException e) {
			System.out.println("Wrong command line arguments");
			printHelp(options);
			System.exit(1);
		}
		if (cmd.hasOption("h")) {
			printHelp(options);
			System.exit(0);
		}
		
		AutoGardeningSystem system = getInstance();
		try {
			system.maxDays = Integer.parseInt(cmd.getOptionValue("d", "60"));
			if (system.maxDays < 45) {
				System.out.println("-d should be at least 45");
				printHelp(options);
				System.exit(1);
			}
			system.speed = Float.parseFloat(cmd.getOptionValue("s", "1"));
		} catch (NumberFormatException e) {
			System.out.println("Wrong command line arguments, number format error");
			printHelp(options);
			System.exit(1);
		}
		Environment.init(system);
		system.run();
	}
	
	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar gardening.jar [-d=50] [-s=10]", options);
	}

	public static AutoGardeningSystem getInstance() {
		if (singleton == null) {
			singleton = new AutoGardeningSystem();
		}
		return singleton;
	}

	private AutoGardeningSystem() {
	}

	private List<GreenHouseUnit> initGreenHouseUnits() {
		List<GreenHouseUnit> units = new ArrayList<>();
		units.add(new GreenHouseUnit(new GrowingPlan("Carrot",
				"10=8:50,18:90,50|30=8:60,18:80,60|8:55,18:75,55", // temperature
				"10=9:0.4,15:0.6,0.4|30=9:0.5,15:0.6,0.5|9:0.45,15:0.65,0.6", // humidity
				"10=8:5,16:40,5|30=8:5,16:45,5|8:6,16:50,6", // light
				"10=8:0.2,18:0.1,0.2|30=8:0.25,18:0.15,0.25|8:0.2,18:0.15,0.2", // waterContent
				"10=7:0,8:20,0|30=7:0,8:30,0|7:0,8:10,0"  // nitrogen
				)));
		units.add(new GreenHouseUnit(new GrowingPlan("Rose",
				"10=8:60,18:85,55|30=8:65,18:70,50|8:55,18:85,65", // temperature
				"10=9:0.3,15:0.5,0.3|30=9:0.4,15:0.5,0.4|9:0.35,15:0.55,0.5", // humidity
				"10=8:15,16:50,15|30=8:15,16:55,15|8:16,16:60,16", // light
				"10=8:0.25,18:0.15,0.25|30=8:0.3,18:0.2,0.3|8:0.25,18:0.2,0.25", // waterContent
				"10=7:0,8:25,0|30=7:0,8:35,0|7:0,8:15,0"  // nitrogen
				)));
		units.add(new GreenHouseUnit(new GrowingPlan("Corn",
				"10=8:55,18:70,55|30=8:50,18:80,65|8:65,18:70,55", // temperature
				"10=9:0.5,15:0.7,0.5|30=9:0.6,15:0.7,0.6|9:0.55,15:0.75,0.7", // humidity
				"10=8:0,16:30,0|30=8:0,16:35,0|8:0,16:40,0", // light
				"10=8:0.15,18:0.05,0.15|30=8:0.20,18:0.10,0.20|8:0.15,18:0.1,0.15", // waterContent
				"10=7:0,8:30,0|30=7:0,8:40,0|7:0,8:20,0"  // nitrogen
				)));
		return units;
	}
	private float time = 0;
	List<GreenHouseUnit> units;
	private void run() {
		logger.info("Auto Gardening System starts running with max days: %d speep: %f", maxDays, speed);
		System.out.printf("Auto Gardening System starts running with max days: %d speed: %f\n", maxDays, speed);
		System.out.println("Simulation is running ... Please check gardening.log for details.");
		
		units = initGreenHouseUnits();
		Thread ui = new Thread(new UIRunner());
		ui.setDaemon(true);
		ui.start();
		
		float deltaT = 1;
		
		while (time < maxDays * Constants.HOURS_PER_DAY - deltaT) {
			time += deltaT;
			for (GreenHouseUnit unit : units) {
				unit.update(time);
			}
			try {
				Thread.sleep((long) (speed*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("\n\nSimulation is complete. Please check gardening.log for details.");
	}

	public int getMaxDays() {
		return maxDays;
	}
	
	public int getMaxHours() {
		return maxDays * Constants.HOURS_PER_DAY;
	}
	
	public String getCurrentTimeString() {
		int time = Math.round(this.time);
		int day = time / Constants.HOURS_PER_DAY;
		int hour = time % Constants.HOURS_PER_DAY;
		return "Day " +day+ " hour " + hour;
	}

	public List<GreenHouseUnit> getUnits() {
		return units;
	}
}
