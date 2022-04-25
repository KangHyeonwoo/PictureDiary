package com.picture.diary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {
        "classpath:nas.properties"
}, ignoreResourceNotFound = true)
@Configuration
public class PropertiesConfig {
}
