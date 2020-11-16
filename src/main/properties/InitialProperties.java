
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

	@Value("${data.event.duration}")
	private String eventDuration;

	@Value("${data.event.intensive}")
	private String eventIntensive;

	@Value("${data.event.accumulation}")
	private String eventAccumulation;

	@Value("${data.event.pattern}")
	private String eventPattern;


	public String getDataRoot() {
		return this.dataRoot;
	}

	public String getCountyPropertiesAdd() {
		return this.dataCountyProperties;
	}

	public String getPngDataRoot() {
		return this.pngDataKey;
	}

	public String getEventDuaration() {
		return this.eventDuration;
	}

	public String getEventIntensive() {
		return this.eventIntensive;
	}

	public String getEventAccumulation() {
		return this.eventAccumulation;
	}

	public String getEventPattern() {
		return this.eventPattern;
	}
}

// ========================================================


