package com.picture.diary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${file.path.data-path}")
	private String dataPath;
	
	@Value("${file.request.url}")
	private String requestUrl;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String filePath = "file://" + dataPath + "/";
		//String filePath = "file:///" + dataPath + "/";
		registry.addResourceHandler(requestUrl)
				.addResourceLocations(filePath);
	}
}
