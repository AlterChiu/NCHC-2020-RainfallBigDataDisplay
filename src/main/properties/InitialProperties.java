
package main.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialProperties {

	/*
	 * initial globalProperties
	 */
	@Value("${data.root}")
	private String dataRoot;

	@Value("${data.county.properties.path}")
	private String dataCountyProperties;

	@Value("${data.pngKey}")
	private String pngDataKey;


	public String getDataRoot() {
		return this.dataRoot;
	}

	public String getCountyPropertiesAdd() {
		return this.dataCountyProperties;
	}

	public String getPngDataRoot() {
		return this.pngDataKey;
	}

}

// ========================================================


