package main;

import java.util.Random;

import growingplan.DailySpec;
import growingplan.WholeLifeSpec;

public class Environment {
	private AutoGardeningSystem system;
	private WholeLifeSpec temperatureSpec;
	private WholeLifeSpec lightSpec;
	private WholeLifeSpec humiditySpec;
	private float[] precipitation;
	private float[] pest;

	private static Environment singleton;

	public static void init(AutoGardeningSystem system) {
		singleton = new Environment(system);
	}

	private float randomTemp(Random r) {
		return 30 + r.nextFloat() * 70;
	}
	
	private float randomLight(Random r) {
		return r.nextInt(50);
	}
	
	private float randomHumidity(Random r) {
		return r.nextFloat();
	}
	
	private float randomPrec(Random r) {
		return (float) (0.1 + r.nextFloat() * 0.4);
	}

	private int randomHourOfDay(Random r) {
		return r.nextInt(Constants.HOURS_PER_DAY);
	}

	private int randomDay(Random r) {
		return r.nextInt(system.getMaxDays());
	}

	private Environment(AutoGardeningSystem system) {
		Random r = new Random(System.currentTimeMillis());
		this.system = system;
		temperatureSpec = new WholeLifeSpec.Builder()
				.appendPeriod(randomDay(r),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomTemp(r))
								.appendPeriod(randomHourOfDay(r), randomTemp(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomTemp(r)).build())
				.appendPeriod(randomDay(r),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomTemp(r))
								.appendPeriod(randomHourOfDay(r), randomTemp(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomTemp(r)).build())
				.appendPeriod(randomDay(r),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomTemp(r))
								.appendPeriod(randomHourOfDay(r), randomTemp(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomTemp(r)).build())
				.appendPeriod(system.getMaxDays(),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomTemp(r))
								.appendPeriod(randomHourOfDay(r), randomTemp(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomTemp(r)).build())
				.build();
		
		lightSpec = new WholeLifeSpec.Builder()
				.appendPeriod(randomDay(r),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomLight(r))
								.appendPeriod(randomHourOfDay(r), randomLight(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomLight(r)).build())
				.appendPeriod(randomDay(r),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomLight(r))
								.appendPeriod(randomHourOfDay(r), randomLight(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomLight(r)).build())
				.appendPeriod(randomDay(r),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomLight(r))
								.appendPeriod(randomHourOfDay(r), randomLight(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomLight(r)).build())
				.appendPeriod(system.getMaxDays(),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomLight(r))
								.appendPeriod(randomHourOfDay(r), randomLight(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomLight(r)).build())
				.build();
		
		humiditySpec = new WholeLifeSpec.Builder()
				.appendPeriod(randomDay(r),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomHumidity(r))
								.appendPeriod(randomHourOfDay(r), randomHumidity(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomHumidity(r)).build())
				.appendPeriod(randomDay(r),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomHumidity(r))
								.appendPeriod(randomHourOfDay(r), randomHumidity(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomHumidity(r)).build())
				.appendPeriod(randomDay(r),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomHumidity(r))
								.appendPeriod(randomHourOfDay(r), randomHumidity(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomHumidity(r)).build())
				.appendPeriod(system.getMaxDays(),
						new DailySpec.Builder().appendPeriod(randomHourOfDay(r), randomHumidity(r))
								.appendPeriod(randomHourOfDay(r), randomHumidity(r))
								.appendPeriod(Constants.HOURS_PER_DAY, randomHumidity(r)).build())
				.build();
		
		precipitation = new float[system.getMaxHours()];
		for (int i = 0 ; i < 10 ; i++) {
			int duration = 1 + r.nextInt(5); // 1-5
			int start = r.nextInt(system.getMaxHours());
			for (int j = 0 ; start + j < system.getMaxHours() && j < duration ; j++) {
				precipitation[start+j] = randomPrec(r);
			}
		}
		
		pest = new float[Constants.HOURS_PER_DAY * system.getMaxHours()];
		for (int i = 0 ; i < 10 ; i++) {
			int start = r.nextInt(system.getMaxHours());
			pest[start] = r.nextFloat();
			
		}
	}

	/**
	 * unit: F
	 * 30 - 100
	 * @param time
	 * @return
	 */
	public static float getTemperature(float time) {
		return singleton.temperatureSpec.getValue(time);
	}

	/**
	 * inches per hour light rain: <0.1 rain: 0.1 - 0.3 heavy rain: >0.3
	 * 0.1-0.5
	 * @param time
	 * @return
	 */
	public static float getPrecipitation(float time) {
		return singleton.precipitation[Math.round(time)];
	}

	/**
	 * 0-1
	 * 
	 * @param time
	 * @return
	 */
	public static float getPestLevel(float time) {
		return singleton.pest[Math.round(time)];
	}

	/**
	 * W per square foot
	 * 0-50
	 * @param time
	 * @return
	 */
	public static float getLight(float time) {
		return singleton.lightSpec.getValue(time);
	}

	/**
	 * value 0-1
	 * 
	 * @param time
	 * @return
	 */
	public static float getHumidity(float time) {
		return singleton.humiditySpec.getValue(time);
	}
}
