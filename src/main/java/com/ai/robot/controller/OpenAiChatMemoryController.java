package com.ai.robot.controller;

import java.net.http.HttpRequest;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.robot.advisors.TokenCountAdvisor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/openai/mem/")
public class OpenAiChatMemoryController {

	private ChatClient openAiClient;

	public OpenAiChatMemoryController(OpenAiChatModel openAiClient, ChatMemory chatMemory) {
		MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
		SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
		OpenAiChatOptions options = OpenAiChatOptions.builder().model(ChatModel.GPT_4_1_NANO).maxCompletionTokens(1000)
				.build();
		this.openAiClient = ChatClient.builder(openAiClient).defaultOptions(options)
				.defaultAdvisors(new TokenCountAdvisor(), messageChatMemoryAdvisor, simpleLoggerAdvisor).build();
	}

	@GetMapping("/chat")
	public ResponseEntity<?> chat(@RequestParam() String message) {
		String response = openAiClient.prompt(message).call().content();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/chat")
	public ResponseEntity<?> userChat(@RequestHeader("username") String username,
			@RequestParam() String message) {
		String response = openAiClient.prompt(message)
				.advisors((advisor) -> advisor.param(ChatMemory.CONVERSATION_ID, username)).call().content();
		return ResponseEntity.ok(response);
	}

}
