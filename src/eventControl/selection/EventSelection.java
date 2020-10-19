
package eventControl.selection;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.properties.CountiesProperties;
import main.properties.InitialProperties;

@RestController
@ComponentScan(basePackages = "main.properties")
public class EventSelection {

	@Autowired
	private InitialProperties globalProperty;

	@Autowired
	private CountiesProperties countiesProperties;

	@Autowired
	private Selection selection;




	/*
	 * get all flood events
	 */
	@GetMapping("/eventSelection/getEvents")
	public String getAllEvents(@RequestParam(value = "county") String county,
			@RequestParam(value = "duration") String duration, @RequestParam(value = "depth") String depth,
			@RequestParam(value = "intensity") String intensity, @RequestParam(value = "pattern") String pattern) {

		if (!this.countiesProperties.getCoutiesMap().containsKey(county)) {
			return "[\"noEvent\"]";
		} else {

			// get filePath
			StringBuilder eventFolderPath = new StringBuilder();
			eventFolderPath.append(this.globalProperty.getDataRoot() + "\\");
			eventFolderPath.append(county + "\\data\\");
			eventFolderPath.append(duration + "\\");
			eventFolderPath.append(depth + "\\");
			eventFolderPath.append(intensity + "\\");
			eventFolderPath.append(pattern + "\\");


			// build eventList in rainfall selected
			JsonArray eventList = new JsonArray();
			for (String event : new File(eventFolderPath.toString()).list()) {
				eventList.add(event);
			}

			if (eventList.size() == 0) {
				eventList.add("noEvent");
			}


			// build return json
			JsonObject outJson = this.selection.getBasicReturnJson(county);
			outJson.add("eventList", eventList);

			return outJson.toString();
		}
	}





	private String getDurationDeploy(String duration) {
		try {
			int durationInt = Integer.parseInt(duration);
			if (durationInt > 72) {
				return "72";
			} else if (durationInt > 48) {
				return "48";
			} else if (durationInt > 24) {
				return "24";
			} else if (durationInt > 12) {
				return "12";
			} else {
				return "-1";
			}
		} catch (Exception e) {
			return "-1";
		}
	}

	private String getDepthDeploy(String depth) {
		try {
			double depthDouble = Double.parseDouble(depth);
			if (depthDouble >= 500) {
				return "500";
			} else if (depthDouble >= 350) {
				return "350";
			} else if (depthDouble >= 200) {
				return "200";
			} else if (depthDouble >= 100) {
				return "100";
			} else {
				return "-1";
			}
		} catch (Exception e) {
			return "-1";
		}
	}

	private String getIntensity(String intensity) {
		try {
			double intensityDouble = Double.parseDouble(intensity);
			if (intensityDouble >= 150) {
				return "150";
			} else if (intensityDouble >= 100) {
				return "100";
			} else if (intensityDouble >= 50) {
				return "50";
			} else if (intensityDouble >= 10) {
				return "10";
			} else {
				return "-1";
			}
		} catch (Exception e) {
			return "-1";
		}
	}










}
