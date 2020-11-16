
package main;

import java.nio.charset.Charset;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;


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

	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		return converter;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(NCHC_RainfallBigDataDisplay.class, args);
	}
}
