package com.ai.robot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(ignoreInvalidFields = true, ignoreUnknownFields = true, prefix = "app.tavily.api")
public class TavilyConfigurations {
	String hostname;
	String key;
}
