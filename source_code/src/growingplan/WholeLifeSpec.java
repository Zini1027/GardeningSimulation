package growingplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import growingplan.DailySpec;
import main.AutoGardeningSystem;
import main.Constants;

public class WholeLifeSpec {

	/**
	 * Parse from a String
	 * @param s format: [ending day a]={DailyPlan 1}|[ending day b]={DailyPlan 2}|{DailyPlan 3}
	 * 		    where {DailyPlan} is defined in DailyPlan.parse()
	 * @return
	 */
	public static WholeLifeSpec parse(String s) {
		try {
			Builder builder = new Builder();
			logger.debug("s:"+s+" "+Arrays.toString(s.split("\\|")));
			for (String part : s.split("\\|")) {
				String[] spilts = part.split("=");
				logger.debug("part: "+part+" " + Arrays.toString(spilts)); 
				if (spilts.length == 1) {
					builder.appendPeriod(AutoGardeningSystem.getInstance().getMaxDays(), DailySpec.parse(spilts[0]));
				} else if (spilts.length == 2) {
					builder.appendPeriod(Integer.parseInt(spilts[0]), DailySpec.parse(spilts[1]));
				} else {
					throw new IllegalArgumentException("Illegal WholeLifePlan input: " + s);
				}
			}
			return builder.build();
		} catch (Exception e) {
			throw new IllegalArgumentException("Error parsing WholeLifePlan: " + s, e);
		}
	}
	
	public static class Builder {

		private List<Pair<Integer, DailySpec>> periods;

		public Builder() {
			periods = new ArrayList<>();
		}

		public Builder appendPeriod(int endingDay, DailySpec value) {
			if (endingDay < 0 || endingDay > AutoGardeningSystem.getInstance().getMaxDays()) {
				throw new IllegalArgumentException("Illegal day: " + endingDay + " should be 0-"
						+ AutoGardeningSystem.getInstance().getMaxDays());
			}
			periods.add(Pair.of(endingDay, value));
			return this;
		}

		public WholeLifeSpec build() {
			Collections.sort(periods, new Comparator<Pair<Integer, DailySpec>>() {
				@Override
				public int compare(Pair<Integer, DailySpec> o1, Pair<Integer, DailySpec> o2) {
					return o1.getLeft().compareTo(o2.getLeft());
				}
			});
			List<Pair<Range<Integer>, DailySpec>> plans = new ArrayList<>();
			int lastEndingDay = 0;
			for (Pair<Integer, DailySpec> p : periods) {
				int currentEndingDay = p.getLeft();
				if (currentEndingDay == lastEndingDay) {
					lastEndingDay = currentEndingDay;
					logger.debug("ignore period ending: " + currentEndingDay);
					continue;
				}
				
				plans.add(Pair.of(Range.between(lastEndingDay, currentEndingDay), p.getRight()));
				lastEndingDay = currentEndingDay;
			}
			return new WholeLifeSpec(plans);
		}
	}

	private WholeLifeSpec(List<Pair<Range<Integer>, DailySpec>> plans) {
		this.plans = plans;
	}

	private static final Logger logger = LogManager.getFormatterLogger();
	List<Pair<Range<Integer>, DailySpec>> plans;

	public float getValue(float time) {
		int timeInt = Math.round(time);
		int day = timeInt / Constants.HOURS_PER_DAY;
		int hourOfDay = timeInt % Constants.HOURS_PER_DAY;
		for (Pair<Range<Integer>, DailySpec> p : plans) {
			Range<Integer> range = p.getLeft();
			if (range.getMinimum() <= day && range.getMaximum() > day) {
				return p.getRight().getValue(hourOfDay);
			}
		}
		logger.warn("Cannot find range for %f", time);
		return 0;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Pair<Range<Integer>, DailySpec> p : plans) {
			sb.append("\t\tday " + p.getLeft().toString() + ":\n" + p.getRight());
		}
		return sb.toString();
	}

}
