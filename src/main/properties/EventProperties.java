
package main.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventProperties {


	@Autowired
	private InitialProperties globalProperty;


	private TreeMap<Double, String> durationKeys;
	private TreeMap<Double, String> intensiveKeys;
	private TreeMap<Double, String> accumulationKeys;
	private Map<Integer, String> patternKeys;


	@Bean
	public void initialEventSelectionGap() throws Exception {
		this.durationKeys = new TreeMap<>();
		String[] durations = this.globalProperty.getEventDuaration().split(",");

		try {
			for (int index = 0; index < 4; index++) {
				double temptDuration = Double.parseDouble(durations[index]);
				this.durationKeys.put(temptDuration, String.format("%d", temptDuration));
			}
		} catch (Exception e) {
			throw new Exception("*ERROR* unable to parse duration");
		}

		// =================================================

		this.intensiveKeys = new TreeMap<>();
		String[] intensives = this.globalProperty.getEventIntensive().split(",");

		try {
			for (int index = 0; index < 4; index++) {
				double temptIntensive = Double.parseDouble(intensives[index]);
				this.intensiveKeys.put(temptIntensive, String.format("%d", temptIntensive));
			}
		} catch (Exception e) {
			throw new Exception("*ERROR* unable to parse intensive");
		}

		// =================================================

		this.accumulationKeys = new TreeMap<>();
		String[] accumulations = this.globalProperty.getEventAccumulation().split(",");

		try {
			for (int index = 0; index < 4; index++) {
				double temptAccumulation = Double.parseDouble(accumulations[index]);
				this.accumulationKeys.put(temptAccumulation, String.format("%d", temptAccumulation));
			}
		} catch (Exception e) {
			throw new Exception("*ERROR* unable to parse accumulation");
		}

		// =================================================

		this.patternKeys = new HashMap<>();
		String[] patterns = this.globalProperty.getEventAccumulation().split(",");

		try {
			for (int index = 0; index < 4; index++) {
				this.patternKeys.put(index, patterns[index]);
			}
		} catch (Exception e) {
			throw new Exception("*ERROR* unable to parse pattern");
		}


	}

	public TreeMap<Double, String> getDurationKeys() {
		return this.durationKeys;
	}

	public TreeMap<Double, String> getIntensiveKeys() {
		return this.intensiveKeys;
	}

	public TreeMap<Double, String> getAccumulationKeys() {
		return this.accumulationKeys;
	}

	public Map<Integer, String> getPatternKeys() {
		return this.patternKeys;
	}

}
