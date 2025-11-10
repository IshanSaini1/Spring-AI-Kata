package com.ai.robot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
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
import com.ai.robot.tools.TimeTools;

@RestController
@RequestMapping("/api/openai/tools")
public class TimeController {

	private TimeTools timeTools;
	private ChatClient chatClient;

	public TimeController(OpenAiChatModel openAiChatModel, JdbcChatMemoryRepository jdbcChatMemoryRepository, TimeTools timeTools) {
		this.timeTools = timeTools;
		ChatMemory chatMemory = MessageWindowChatMemory.builder().chatMemoryRepository(jdbcChatMemoryRepository)
				.maxMessages(5).build();
		OpenAiChatOptions options = OpenAiChatOptions.builder().model(ChatModel.GPT_4_1_NANO).maxCompletionTokens(1000)
				.build();
		MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
		this.chatClient = ChatClient.builder(openAiChatModel).defaultOptions(options)
				.defaultAdvisors(new TokenCountAdvisor(), messageChatMemoryAdvisor).build();
	}

	@GetMapping("/current-time")
	public ResponseEntity<String> localTime(@RequestHeader("username") String username,
			@RequestParam("message") String message) {
		String response = chatClient.prompt()
				.tools(timeTools)
				.user(message)
				.advisors(t -> t.param(ChatMemory.CONVERSATION_ID, username)).call()
				.content();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/current-time/all")
	public ResponseEntity<String> localTimeAllRegions(@RequestHeader("username") String username,
			@RequestParam("message") String message) {
		String response = chatClient.prompt()
				.tools(timeTools)
				.user(message)
				.advisors(t -> t.param(ChatMemory.CONVERSATION_ID, username)).call()
				.content();
		return ResponseEntity.ok(response);
	}

}
