package growingplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.Constants;

public class DailySpec {

	/**
	 * Parse from a String
	 * @param s format: [ending hour 1]:[value],[ending hour 2]:[value],[value]
	 * @return
	 */
	public static DailySpec parse(String s) {
		try {
			Builder builder = new Builder();
			String[] parts = s.split(",");
			for (String part : parts) {
				String[] spilts = part.split(":");
				if (spilts.length == 1) {
					builder.appendPeriod(Constants.HOURS_PER_DAY, Float.parseFloat(spilts[0]));
				} else if (spilts.length == 2) {
					builder.appendPeriod(Integer.parseInt(spilts[0]), Float.parseFloat(spilts[1]));
				} else {
					throw new IllegalArgumentException("Illegal DailyPlan input: " + s);
				}
			}
			return builder.build();
		} catch (Exception e) {
			throw new IllegalArgumentException("Error parsing DailyPlan: " + s, e);
		}
	}
	
	public static class Builder {

		private List<Pair<Integer, Float>> periods;

		public Builder() {
			periods = new ArrayList<>();
		}

		public Builder appendPeriod(int endingHourOfDay, float value) {
			if (endingHourOfDay > Constants.HOURS_PER_DAY || endingHourOfDay < 0) {
				throw new IllegalArgumentException(
						"Illegal hour of day: " + endingHourOfDay + " should be 0-" + Constants.HOURS_PER_DAY);
			}
			periods.add(Pair.of(endingHourOfDay, value));
			return this;
		}

		public DailySpec build() {
			Collections.sort(periods, new Comparator<Pair<Integer, Float>>() {
				@Override
				public int compare(Pair<Integer, Float> o1, Pair<Integer, Float> o2) {
					return o1.getLeft().compareTo(o2.getLeft());
				}
			});
			List<Pair<Range<Integer>, Float>> plans = new ArrayList<>();
			int lastEndingHourOfDay = 0;
			for (Pair<Integer, Float> p : periods) {
				int currentEndingHourOfDay = p.getLeft();
				if (currentEndingHourOfDay == lastEndingHourOfDay) {
					// ignore
					lastEndingHourOfDay = currentEndingHourOfDay;
					logger.debug("ignore period ending: " + currentEndingHourOfDay);
					continue;
				}
				plans.add(Pair.of(Range.between(lastEndingHourOfDay, currentEndingHourOfDay), p.getRight()));
				lastEndingHourOfDay = currentEndingHourOfDay;
			}
			return new DailySpec(plans);
		}
	}

	private static final Logger logger = LogManager.getFormatterLogger();
	private List<Pair<Range<Integer>, Float>> plans;

	private DailySpec(List<Pair<Range<Integer>, Float>> plans) {
		this.plans = plans;
	}

	float getValue(int hourOfDay) {
		for (Pair<Range<Integer>, Float> p : plans) {
			Range<Integer> range = p.getLeft();
			if (range.getMinimum() <= hourOfDay && range.getMaximum() > hourOfDay) {
				return p.getRight();
			}
		}
		logger.warn("Cannot find range for %d", hourOfDay);
		return 0;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Pair<Range<Integer>, Float> p : plans) {
			sb.append("\t\t\thour " + p.getLeft().toString() + ": " + p.getRight() + "\n");
		}
		return sb.toString();
	}
}
