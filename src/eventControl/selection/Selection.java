
package eventControl.selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import main.properties.CountiesProperties;
import main.properties.CountiesProperties.CountryProperty;


@Component
@ComponentScan(basePackages = "main.properties")
public class Selection {

	@Autowired
	private CountiesProperties countiesProperties;

	public JsonObject getBasicReturnJson(String county) {
		// get county properties bound , zooming
		JsonObject outJson = new JsonObject();

		try {
			CountryProperty properties = this.countiesProperties.getCoutiesMap().get(county);
			outJson = properties.getJson();
		} catch (Exception e) {

			outJson.addProperty("id", "noCounty");
			outJson.addProperty("name", "noCounty");

		}
		return outJson;
	}
}
