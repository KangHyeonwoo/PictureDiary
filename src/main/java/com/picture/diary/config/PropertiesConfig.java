package com.picture.diary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {
        "classpath:extract.properties"
}, ignoreResourceNotFound = true)
@Configuration
public class PropertiesConfig {
}
