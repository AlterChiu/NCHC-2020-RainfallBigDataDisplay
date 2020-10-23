
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
import main.properties.EventProperties;
import main.properties.InitialProperties;

@RestController
@ComponentScan(basePackages = "main.properties")
public class EventSelection {

	@Autowired
	private InitialProperties globalProperty;

	@Autowired
	private CountiesProperties countiesProperties;

	@Autowired
	private EventProperties eventProperties;

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

			// get event selection key
			StringBuilder eventKey = new StringBuilder();
			eventKey.append(duration + "_");
			eventKey.append(intensity + "_");
			eventKey.append(depth + "_");
			eventKey.append(pattern);



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


	@GetMapping("/eventSelection/getEventProperties")
	public String getEventProperties(String contries) {
		JsonObject outJson = new JsonObject();

		// duration
		JsonArray durationArray = new JsonArray();
		this.eventProperties.getDurationKeys().keySet().forEach(key -> {
			durationArray.add(this.eventProperties.getDurationKeys().get(key));
		});

		// intensive
		JsonArray intensiveArray = new JsonArray();
		this.eventProperties.getIntensiveKeys().keySet().forEach(key -> {
			intensiveArray.add(this.eventProperties.getIntensiveKeys().get(key));
		});

		// accumulation
		JsonArray accumulationArray = new JsonArray();
		this.eventProperties.getAccumulationKeys().keySet().forEach(key -> {
			accumulationArray.add(this.eventProperties.getAccumulationKeys().get(key));
		});

		// pattern
		JsonObject patternObject = new JsonObject();
		this.eventProperties.getPatternKeys().keySet().forEach(key -> {
			patternObject.addProperty(this.eventProperties.getPatternKeys().get(key), key);
		});


		// combining
		outJson.add("duration", durationArray);
		outJson.add("intensive", intensiveArray);
		outJson.add("accumulation", accumulationArray);
		outJson.add("pattern", patternObject);

		return outJson.toString();
	}






}
