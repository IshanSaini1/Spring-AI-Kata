package com.ai.robot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.robot.constants.PromptDictionary;
import com.ai.robot.constants.PromptDictionary.PromptName;

@RestController
@RequestMapping("/api/template/openai")
public class PromptTemplateController {

	@Value("classpath:/prompt-template/email-support-prompt.st")
	Resource promptTemplate;
	
	ChatClient chatClient;

	public PromptTemplateController(OpenAiChatModel openAiChatModel) {
		this.chatClient = ChatClient.builder(openAiChatModel).build();
	}
	
	@GetMapping("/email")
	public String emailResponse(@RequestParam("customerName") String customerName, @RequestParam("customerMessage") String customerMessage) {
		return chatClient
				.prompt()
				.system(PromptDictionary.getInstance().getPromptMap().get(PromptName.EMAIL_SUPPORT))
				.user((t) -> t
						.text(promptTemplate)
						.param("customerName", customerName)
						.param("customerMessage", customerMessage)
						)
				.call()
				.content();
	}
}
