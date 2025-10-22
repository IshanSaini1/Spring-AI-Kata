package com.ai.robot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/stream/openai")
public class OpenAiStreamController {

	private ChatClient chatClient;

	public OpenAiStreamController(OpenAiChatModel openAiChatModel) {
		OpenAiChatOptions options = OpenAiChatOptions.builder().model(ChatModel.GPT_4_1_NANO).build();
		this.chatClient = ChatClient.builder(openAiChatModel).defaultOptions(options).build();
	}

	@GetMapping("/chat")
	public Flux<String> chat(@RequestParam() String message) {
		return chatClient.prompt(message).stream().content();
	}
}
