package com.ai.robot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.robot.advisors.TokenCountAdvisor;
import com.ai.robot.tools.HelpDeskTools;
import com.ai.robot.tools.TimeTools;

@RestController
@RequestMapping("/api/openai/tools/helpdesk")
public class HelpDeskController {

	private ChatClient chatClient;
	
	private HelpDeskTools helpDeskTools;
	
	@Value("classpath:/prompt-template/helpdeskSystemPromptTemplate.st")
	Resource helpDeskSystemPromptTemplate;
	
	public HelpDeskController(OpenAiChatModel openAiChatModel, JdbcChatMemoryRepository jdbcChatMemoryRepository, TimeTools timeTools, HelpDeskTools helpDeskTools) {
		// Model Config Options
		ChatOptions modelOptions = OpenAiChatOptions.builder().maxCompletionTokens(5000).model(ChatModel.GPT_4_1_MINI).build();
		
		// Token Count Advisor
		TokenCountAdvisor tokenCountAdvisor = new TokenCountAdvisor();
		
		//Simple Logger Advisor
		SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
		
		// Introducing memory to the model 
		MessageWindowChatMemory chatMemoryWindow = MessageWindowChatMemory.builder().chatMemoryRepository(jdbcChatMemoryRepository).maxMessages(3).build();
		MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemoryWindow).build();
		
		this.helpDeskTools = helpDeskTools;
		
		// Create and set the chat client.
		this.chatClient = ChatClient.builder(openAiChatModel)
				.defaultTools(timeTools)
				.defaultOptions(modelOptions)
				.defaultAdvisors(tokenCountAdvisor, simpleLoggerAdvisor, messageChatMemoryAdvisor)
				.build();
	}
	
	@GetMapping("/main")
	public ResponseEntity<String> helpDesk(@RequestHeader("username") String username, @RequestParam("message") String message){
		Map<String,Object> toolContext = new HashMap<>();
		toolContext.put("username", username);
		
		String answer = chatClient.prompt()
				.user(message)
				.system(helpDeskSystemPromptTemplate)
				.advisors(t->t.param(ChatMemory.CONVERSATION_ID, username))
				.tools(helpDeskTools)
				.toolContext(toolContext).call().content();
		
		return ResponseEntity.ok(answer);
	}
}
