package ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import growingplan.WholeLifeSpec;
import main.AutoGardeningSystem;
import main.GreenHouseUnit;

public class UIRunner implements Runnable {
	private static final Logger logger = LogManager.getFormatterLogger();
	private static final String PLAN_HELP = "[ending day a]=[ending hour 1]:[value],[ending hour 2]:[value],[value]|[ending day b]=[ending hour 1]:[value],[ending hour 2]:[value],[value]|[ending hour 1]:[value],[ending hour 2]:[value],[value]"
			+ "\nFor example \"10=8:50,18:90,50|30=8:60,18:80,60|8:55,18:75,55\" means"
			+ "\nFrom day 0 to day 9: hour 0 to 7 value is 50, hour 8 to 17 value is 90, hour 18 to 23 value is 50"
			+ "\nFrom day 10 to day 29: hour 0 to 7 value is 60, hour 8 to 17 value is 80, hour 18 to 23 value is 60"
			+ "\nFrom day 30 to last day: hour 0 to 7 value is 55, hour 8 to 17 value is 75, hour 18 to 23 value is 55\n";

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter("\n");
		while (true) {

			System.out.println("To change growing plan, choose number below:");
			List<GreenHouseUnit> units = AutoGardeningSystem.getInstance().getUnits();
			int i = 0;
			for (GreenHouseUnit unit : units) {
				System.out.printf("Type \"%d\" to change growing plan of GreenHouseUnit \"%s\"\n", i++,
						unit.getGrowingPlan().getPlantName());
			}
			int input = inputNumberBetween(scanner, 0, units.size());

			GreenHouseUnit unit = units.get(input);
			System.out.printf("Current growing plan for \"%s\" is:\n%s", unit.getGrowingPlan().getPlantName(),
					unit.getGrowingPlan());
			i = 0;
			System.out.printf("Type \"%d\" to change \"Temperature\" plan\n", i++);
			System.out.printf("Type \"%d\" to change \"Humidity\" plan\n", i++);
			System.out.printf("Type \"%d\" to change \"Light\" plan\n", i++);
			System.out.printf("Type \"%d\" to change \"WaterContent\" plan\n", i++);
			System.out.printf("Type \"%d\" to change \"Nitrogen\" plan\n", i++);
			input = inputNumberBetween(scanner, 0, i);
			System.out.printf("Please enter the new plan in the following format:\n%s", PLAN_HELP);
			String plan = "";
			WholeLifeSpec spec = null;
			while (true) {
				try {
					System.out.printf("Type the plan:");
					plan = scanner.next();
					spec = WholeLifeSpec.parse(plan);
					break;
				} catch (IllegalArgumentException e) {
					System.out.println("Incorrect format: " + e.getMessage());
				}
			}

			String planName = "";
			switch (input) {
			case 0:
				unit.getGrowingPlan().setTemperaturePlan(spec);
				planName = "Temperature";
				break;
			case 1:
				unit.getGrowingPlan().setHumidityPlan(spec);
				planName = "Humidity";
				break;
			case 2:
				unit.getGrowingPlan().setLightPlan(spec);
				planName = "Light";
				break;
			case 3:
				unit.getGrowingPlan().setWaterContentPlan(spec);
				planName = "WaterContent";
				break;
			case 4:
				unit.getGrowingPlan().setNitrogenPlan(spec);
				planName = "Nitrogen";
				break;
			}
			String logMsg = String.format("Current time:%s Changed \"%s\" plan of GreenHouseUnit \"%s\" to:\n%s",
					AutoGardeningSystem.getInstance().getCurrentTimeString(), planName,
					unit.getGrowingPlan().getPlantName(), spec);
			System.out.printf("%s\nThis event is also logged.\n", logMsg);
			logger.info(logMsg);
		}
	}

	private int inputNumberBetween(Scanner scanner, int fromInc, int toExc) {
		int input = 0;
		while (true) {
			try {
				System.out.printf("Type a number between %d(inclusive) and %d(exclusive):", fromInc, toExc);
				input = scanner.nextInt();
				if (input < 0 || input >= toExc) {
					System.out.println("Please type a legal number.");
				} else {
					break;
				}
			} catch (InputMismatchException e) {
				scanner.next();
				System.out.println("Please type a number.");
			}
		}
		return input;
	}

}
