package com.ai.robot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai")
public class PromptStuffingController {
	
	@Value("classpath:/prompt-template/system-HR-prompt-file.st")
	Resource hrPolicy;

	ChatClient chatClient;

	public PromptStuffingController(OpenAiChatModel openAiChatModel) {
		this.chatClient = ChatClient.builder(openAiChatModel).build();
	}

	@GetMapping("/prompt-stuffing")
	public String doPromptStuffing(@RequestParam("message") String message) {
		return chatClient
				.prompt(message)
				.system(hrPolicy)
				.call()
				.content();
	}
}
