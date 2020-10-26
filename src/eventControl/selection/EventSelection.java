
package eventControl.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.properties.CountiesProperties;
import main.properties.EventProperties;

@RestController
@ComponentScan(basePackages = "main.properties")
public class EventSelection {


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
			@RequestParam(value = "duration") String duration,
			@RequestParam(value = "accumulation") String accumulation,
			@RequestParam(value = "intensitve") String intensitve, @RequestParam(value = "pattern") String pattern) {

		if (!this.countiesProperties.getCoutiesMap().containsKey(county)) {
			return "[\"noEvent\"]";
		} else {

			// get event selection key
			StringBuilder eventKey = new StringBuilder();
			eventKey.append(duration + "_");
			eventKey.append(intensitve + "_");
			eventKey.append(accumulation + "_");
			eventKey.append(pattern);

			List<String> eventList = Optional
					.ofNullable(
							this.countiesProperties.getCoutiesMap().get(county).getEventMap().get(eventKey.toString()))
					.orElse(new ArrayList<>());

			JsonArray eventArray = new JsonArray();
			eventList.forEach(e -> eventArray.add(e));

			// build return json
			JsonObject outJson = this.selection.getBasicReturnJson(county);
			outJson.add("eventList", eventArray);

			return outJson.toString();
		}
	}


	@GetMapping("/eventSelection/getEventProperties")
	public String getEventProperties() {
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
