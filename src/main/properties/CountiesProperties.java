
package main.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import usualTool.AtFileReader;


@Configuration
public class CountiesProperties {

	@Autowired
	private InitialProperties globalProperty;

	@Autowired
	private EventProperties eventProperties;

	/*
	 * setting counties to show
	 */
	// key in English
	private Map<String, CountryProperty> coutiesMap;

	public Map<String, CountryProperty> getCoutiesMap() {
		return this.coutiesMap;
	}





	@Bean
	public void initialCoutiesMap() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		this.coutiesMap = new LinkedHashMap<>();

		// read counties file
		JsonArray coutiesArray = new AtFileReader(this.globalProperty.getCountyPropertiesAdd()).getJson()
				.getAsJsonArray();

		// counties = {Chinese , English}
		for (JsonElement countyElement : coutiesArray) {
			JsonObject countyObject = countyElement.getAsJsonObject();

			// key in English
			CountryProperty temptProperties = new CountryProperty();
			temptProperties.setID(countyObject.get("id").getAsString());
			temptProperties.setName(countyObject.get("name").getAsString());
			temptProperties.setAmount(countyObject.get("amount").getAsInt());
			temptProperties.setJson(countyObject);

			// rainfall
			JsonObject floodObject = countyObject.get("flood").getAsJsonObject();
			temptProperties.setFloodMaxX(floodObject.get("maxX").getAsDouble());
			temptProperties.setFloodMaxY(floodObject.get("maxY").getAsDouble());
			temptProperties.setFloodMinX(floodObject.get("minX").getAsDouble());
			temptProperties.setFloodMinY(floodObject.get("minY").getAsDouble());

			// flood
			JsonObject rainfallObject = countyObject.get("rainfall").getAsJsonObject();
			temptProperties.setRainfallMaxX(rainfallObject.get("maxX").getAsDouble());
			temptProperties.setRainfallMaxY(rainfallObject.get("maxY").getAsDouble());
			temptProperties.setRainfallMinX(rainfallObject.get("minX").getAsDouble());
			temptProperties.setRainfallMinY(rainfallObject.get("minY").getAsDouble());


			// get country event configFile
			String configFile = "";
			for (String fileName : new File(this.globalProperty.getDataRoot()).list()) {
				if (fileName.toLowerCase().contains("metadata.csv")) {
					configFile = this.globalProperty.getDataRoot() + fileName;
					break;
				}
			}

			// set country events
			try {
				setCountyEvent(configFile, temptProperties);
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.coutiesMap.put(temptProperties.getID(), temptProperties);
		}
	}

	private void setCountyEvent(String configFilePath, CountryProperty temptCounty) throws IOException {
		// set event
		String configContent[][] = new AtFileReader(configFilePath).getCsv(1, 0);

		for (String[] temptLine : configContent) {
			try {
				String eventID = temptLine[0].split("_")[0] + temptLine[0].split("_")[1];
				double duration = Double.parseDouble(temptLine[1]);
				double intensive = Double.parseDouble(temptLine[2]);
				double accumulation = Double.parseDouble(temptLine[6]);
				int pattern = Integer.parseInt(temptLine[7]);

				// formatting key
				String durationKey = this.eventProperties.getDurationKeys()
						.get(Optional.ofNullable(this.eventProperties.getDurationKeys().lowerKey(duration))
								.orElse(this.eventProperties.getDurationKeys().firstKey()));

				String intensiveKey = this.eventProperties.getIntensiveKeys()
						.get(Optional.ofNullable(this.eventProperties.getIntensiveKeys().lowerKey(intensive))
								.orElse(this.eventProperties.getIntensiveKeys().firstKey()));


				String accumulationKey = this.eventProperties.getAccumulationKeys()
						.get(Optional.ofNullable(this.eventProperties.getAccumulationKeys().lowerKey(accumulation))
								.orElse(this.eventProperties.getAccumulationKeys().firstKey()));

				String patternKey;
				if (this.eventProperties.getPatternKeys().containsKey(pattern)) {
					patternKey = String.valueOf(pattern);
				} else {
					patternKey = String.valueOf(0);
				}

				// add to country properties
				temptCounty.addEvent(eventID, durationKey, intensiveKey, accumulationKey, patternKey);

			} catch (Exception e) {
				System.out
						.println("*WARN* unable to parse event property, " + temptCounty.getID() + "_" + temptLine[0]);
			}
		}
	}

	public class CountryProperty {

		private String id;
		private String name;
		private int amount;
		private JsonObject countyObject;

		private double floodMaxX;
		private double floodMaxY;
		private double floodMinX;
		private double floodMinY;

		private double rainfallMaxX;
		private double rainfallMaxY;
		private double rainfallMinX;
		private double rainfallMinY;

		private Map<String, List<String>> eventMapping = new HashMap<>();


		public void setID(String id) {
			this.id = id;
		}

		public String getID() {
			return this.id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setJson(JsonObject json) {
			this.countyObject = json;
		}

		public JsonObject getJson() {
			return this.countyObject;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		public int getAmount() {
			return this.amount;
		}

		public void setFloodMaxX(double maxX) {
			this.floodMaxX = maxX;
		}

		public double getFloodMaxX() {
			return this.floodMaxX;
		}

		public void setFloodMaxY(double maxY) {
			this.floodMaxY = maxY;
		}

		public double getFloodMaxY() {
			return this.floodMaxY;
		}

		public void setFloodMinX(double minX) {
			this.floodMinX = minX;
		}

		public double getFloodMinX() {
			return this.floodMinX;
		}

		public void setFloodMinY(double minY) {
			this.floodMinY = minY;
		}

		public double getFloodMinY() {
			return this.floodMinY;
		}

		public void setRainfallMaxX(double maxX) {
			this.rainfallMaxX = maxX;
		}

		public double getMaxX() {
			return this.rainfallMaxX;
		}

		public void setRainfallMaxY(double maxY) {
			this.rainfallMaxY = maxY;
		}

		public double getMaxY() {
			return this.rainfallMaxY;
		}

		public void setRainfallMinX(double minX) {
			this.rainfallMinX = minX;
		}

		public double getMinX() {
			return this.rainfallMinX;
		}

		public void setRainfallMinY(double minY) {
			this.rainfallMinY = minY;
		}

		public double getMinY() {
			return this.rainfallMinY;
		}

		public void addEvent(String eventID, String duration, String intensive, String accumulation, String pattern) {
			StringBuilder eventKey = new StringBuilder();
			eventKey.append(duration + "_");
			eventKey.append(intensive + "_");
			eventKey.append(accumulation + "_");
			eventKey.append(pattern);

			Optional.ofNullable(this.eventMapping.get(eventKey.toString())).ifPresentOrElse(e -> {
				this.eventMapping.get(eventKey.toString()).add(eventID);
			}, () -> {
				List<String> temptList = new ArrayList<>();
				temptList.add(eventID);
				this.eventMapping.put(eventKey.toString(), temptList);
			});
		}

		public Map<String, List<String>> getEventMap() {
			return this.eventMapping;
		}
	}
}









