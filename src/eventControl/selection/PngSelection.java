
package eventControl.selection;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.properties.CountiesProperties;
import main.properties.InitialProperties;

@RestController
public class PngSelection {

	@Autowired
	private InitialProperties globalProperty;

	@Autowired
	private CountiesProperties countiesProperties;

	@Autowired
	private Selection selection;

	/*
	 * get one step flood and rainfall pngFile
	 */
	@GetMapping("/eventSelection/getPNG/getRainfallFlood")
	public String getRainfallFloodPNG(@RequestParam(value = "county") String county,
			@RequestParam(value = "eventID") String eventID) {


		if (!this.countiesProperties.getCoutiesMap().containsKey(county)) {
			return "{id:\"no such county\"}";
		} else {

			eventID = "Event_" + String.format("%05d", Integer.parseInt(eventID));
			String eventFolder = county + "\\data\\" + eventID + "\\";


			String dataEventFolder = this.globalProperty.getDataRoot() + "\\" + eventFolder;
			String pngLinkUrl = "..\\" + this.globalProperty.getPngDataRoot() + "\\" + eventFolder;


			// build return json
			JsonObject outJson = this.selection.getBasicReturnJson(county);
			JsonArray pngArray = new JsonArray();

			// get image url
			for (int timeStep = 0; timeStep < new File(dataEventFolder + "\\rainfall\\").list().length; timeStep++) {
				String rainfallImage = pngLinkUrl + "rainfall\\" + timeStep + ".png";
				String floodImage = pngLinkUrl + "flood\\" + timeStep + ".png";

				JsonObject temptObject = new JsonObject();
				temptObject.addProperty("rainfallUrl", rainfallImage);
				temptObject.addProperty("floodUrl", floodImage);
				pngArray.add(temptObject);
			}
			outJson.add("pngUrl", pngArray);

			return outJson.toString();
		}
	}
}
