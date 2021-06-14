package com.picture.diary.config;

import com.picture.diary.extract.data.FilePathProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {FilePathProperties.class})
public class PropertiesConfig {
}
