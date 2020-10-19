
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
			@RequestParam(value = "duration") String duration, @RequestParam(value = "depth") String depth,
			@RequestParam(value = "intensity") String intensity, @RequestParam(value = "pattern") String pattern,
			@RequestParam(value = "eventID") String EventID) {


		if (!this.countiesProperties.getCoutiesMap().containsKey(county)) {
			return "{id:\"no such county\"}";
		} else {

			// get filePath
			StringBuilder eventFolderPath = new StringBuilder();
			eventFolderPath.append(county + "\\data\\");
			eventFolderPath.append(duration + "\\");
			eventFolderPath.append(depth + "\\");
			eventFolderPath.append(intensity + "\\");
			eventFolderPath.append(pattern + "\\");
			eventFolderPath.append(EventID + "\\");

			String dataEventFolder = this.globalProperty.getDataRoot() + "\\" + eventFolderPath.toString();
			String pngLinkUrl = "..\\" + this.globalProperty.getPngDataRoot() + "\\";


			// build return json
			JsonObject outJson = this.selection.getBasicReturnJson(county);
			JsonArray pngArray = new JsonArray();

			// get image url
			for (int timeStep = 0; timeStep < new File(dataEventFolder + "\\rainfall\\").list().length; timeStep++) {
				String rainfallImage = pngLinkUrl + eventFolderPath.toString() + "rainfall\\" + timeStep + ".png";
				String floodImage = pngLinkUrl + eventFolderPath.toString() + "flood\\" + timeStep + ".png";

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
