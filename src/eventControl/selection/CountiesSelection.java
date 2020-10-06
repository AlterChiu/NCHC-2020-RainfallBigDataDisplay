
package eventControl.selection;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.JsonObject;
import main.properties.CountiesProperties;
import main.properties.CountiesProperties.CountryProperty;

@RestController
@ComponentScan(basePackages = "main.properties")
public class CountiesSelection {

	@Autowired
	private CountiesProperties countiesProperties;

	@Autowired
	private Selection selection;

	@GetMapping("/eventSelection/getCounties")
	public String getCountiesIdName() {

		Map<String, CountryProperty> countiesMap = this.countiesProperties.getCoutiesMap();

		JsonObject outJson = new JsonObject();
		countiesMap.keySet().forEach(county -> {
			outJson.add(county, selection.getBasicReturnJson(county));
		});

		if (outJson.size() == 0) {
			JsonObject temptJson = new JsonObject();
			temptJson.addProperty("name", "noEvent");
		}

		return outJson.toString();
	}

}
