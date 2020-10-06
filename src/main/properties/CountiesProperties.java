
package main.properties;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import usualTool.AtFileReader;


@Configuration
public class CountiesProperties {

	@Autowired
	private InitialProperties globalProperty;

	/*
	 * setting counties to show
	 */
	// key in English
	private Map<String, CountryProperty> coutiesMap;

	public Map<String, CountryProperty> getCoutiesMap() {
		return this.coutiesMap;
	}

	@Bean
	public void initialCoutiesMap() {
		this.coutiesMap = new LinkedHashMap<>();
		String[][] couties = null;

		try {

			// read counties file
			couties = new AtFileReader(this.globalProperty.getCountyPropertiesAdd()).getCsv(1, 0);

			// counties = {Chinese , English}
			for (String county[] : couties) {
				// key in English
				CountryProperty temptProperties = new CountryProperty();
				temptProperties.setID(county[1]);
				temptProperties.setName(county[0]);
				temptProperties.setX(Double.parseDouble(county[2]));
				temptProperties.setY(Double.parseDouble(county[3]));
				temptProperties.setZoom(Integer.parseInt(county[4]));

				temptProperties.setMaxX(Double.parseDouble(county[5]));
				temptProperties.setMaxY(Double.parseDouble(county[6]));
				temptProperties.setMinX(Double.parseDouble(county[7]));
				temptProperties.setMinY(Double.parseDouble(county[8]));

				this.coutiesMap.put(temptProperties.getID(), temptProperties);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class CountryProperty {

		private String id;
		private String name;
		private double wgs84_X;
		private double wgs84_Y;
		private int zoom;

		private double maxX;
		private double maxY;
		private double minX;
		private double minY;

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

		public void setX(double x) {
			try {
				if (x >= 180 || x <= -180) {
					this.wgs84_X = 121;
				} else {
					this.wgs84_X = x;
				}
			} catch (Exception e) {
				this.wgs84_X = 121;
			}
		}

		public double getX() {
			return this.wgs84_X;
		}

		public void setY(double y) {
			try {
				if (y >= 90 || y < -90) {
					this.wgs84_Y = 23.6;
				} else {
					this.wgs84_Y = y;
				}
			} catch (Exception e) {
				this.wgs84_Y = 23.6;
			}
		}

		public double getY() {
			return this.wgs84_Y;
		}

		public void setZoom(int z) {
			try {
				if (z < 1 || z > 18) {
					this.zoom = 7;
				} else {
					this.zoom = z;
				}
			} catch (Exception e) {
				this.zoom = 7;
			}
		}

		public int getZoom() {
			return this.zoom;
		}

		public void setMaxX(double maxX) {
			this.maxX = maxX;
		}

		public double getMaxX() {
			return this.maxX;
		}

		public void setMaxY(double maxY) {
			this.maxY = maxY;
		}

		public double getMaxY() {
			return this.maxY;
		}

		public void setMinX(double minX) {
			this.minX = minX;
		}

		public double getMinX() {
			return this.minX;
		}

		public void setMinY(double minY) {
			this.minY = minY;
		}

		public double getMinY() {
			return this.minY;
		}
	}


}
