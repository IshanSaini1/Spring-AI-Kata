package com.ai.robot.configuration;

import org.springframework.ai.model.SimpleApiKey;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ModelConfiguration {

	@Value(value = "${spring.ai.openai.api-key}")
	private String OPEN_AI_API_KEY;

	@Bean
	OpenAiChatModel openAiChatModel() {
		OpenAiChatModel chatModel = OpenAiChatModel.builder()
				.openAiApi(OpenAiApi.builder().apiKey(new SimpleApiKey(OPEN_AI_API_KEY)).build())
				.build();
		return chatModel;
	}

	// AWS Bedrock uses the Converse API under its hood so learn that to build this.
	/*
	 * @Bean BedrockProxyChatModel bedrockProxyChatModel() { BedrockProxyChatModel
	 * chatModel = BedrockProxyChatModel.builder()
	 * .bedrockRuntimeClient(BedrockRuntimeClient.builder()
	 * .credentialsProvider(DefaultCredentialsProvider.builder().build())
	 * .region(Region.US_EAST_1).build())
	 * .defaultOptions(BedrockChatOptions.builder().model("amazon.nova-micro-v1:0").
	 * build()).build(); return chatModel; }
	 */

}
