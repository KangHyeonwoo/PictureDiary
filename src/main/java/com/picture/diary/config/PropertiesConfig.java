package com.picture.diary.config;

import com.picture.diary.extract.data.PicturePathProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {PicturePathProperties.class})
public class PropertiesConfig {
}
