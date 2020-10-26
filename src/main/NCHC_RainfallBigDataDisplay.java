
package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@ConfigurationProperties
@PropertySource(value = { "classpath:initailSetting.properties" }, ignoreResourceNotFound = true, encoding = "UTF-8")

@ComponentScan(basePackages = "eventControl")
@ComponentScan(basePackages = "main.properties")
public class NCHC_RainfallBigDataDisplay extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NCHC_RainfallBigDataDisplay.class);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(NCHC_RainfallBigDataDisplay.class, args);
	}
}
