package com.ai.robot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.robot.advisors.TokenCountAdvisor;
import com.ai.robot.constants.PromptDictionary;
import com.ai.robot.constants.PromptDictionary.PromptName;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

	private ChatClient openAiClient;

	public OpenAIController(OpenAiChatModel openAiClient) {
		OpenAiChatOptions options = OpenAiChatOptions.builder().model(ChatModel.GPT_4_1_NANO).maxCompletionTokens(500)
				.build();

		this.openAiClient = ChatClient.builder(openAiClient).defaultOptions(options)
				.defaultAdvisors(new TokenCountAdvisor()).build();
	}

	@GetMapping("/chat")
	public String openAiChat(@RequestParam() String message) {
		return openAiClient.prompt(message).call().content();
	}

	@GetMapping("/hr")
	public String openAiChatHr(@RequestParam() String message) {
		return openAiClient.prompt(message).advisors(new TokenCountAdvisor(), new SimpleLoggerAdvisor())
				.system(PromptDictionary.getInstance().getPromptMap().get(PromptName.HR)).call().content();
	}

	@GetMapping("/chat-response")
	public String openAiChatResponseChat(@RequestParam() String message) {
		ChatClientResponse reponse = openAiClient.prompt(message).call().chatClientResponse();
		return reponse.chatResponse().getResult().toString();
	}

}
