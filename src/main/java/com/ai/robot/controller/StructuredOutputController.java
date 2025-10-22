package com.ai.robot.controller;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.robot.advisors.TokenCountAdvisor;
import com.ai.robot.model.CountryCitiesPOJO;

@RestController
@RequestMapping("/api/openai/so/")
public class StructuredOutputController {

	private ChatClient openAiClient;

	public StructuredOutputController(OpenAiChatModel openAiClient) {
		OpenAiChatOptions options = OpenAiChatOptions.builder().model(ChatModel.GPT_4_1_NANO).maxCompletionTokens(1000)
				.build();

		this.openAiClient = ChatClient.builder(openAiClient).defaultOptions(options)
				.defaultAdvisors(new TokenCountAdvisor()).build();
	}

	@GetMapping("/city")
	public ResponseEntity<CountryCitiesPOJO> cityChatBean(@RequestParam() String message) {
		CountryCitiesPOJO response = openAiClient.prompt().user(message).call().entity(CountryCitiesPOJO.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/list-of-cities")
	public ResponseEntity<List<String>> getListOfCityFromCountry(@RequestParam() String country) {
		List<String> response = openAiClient.prompt().user(country).call().entity(new ListOutputConverter());
		return ResponseEntity.ok(response);
	}
}
