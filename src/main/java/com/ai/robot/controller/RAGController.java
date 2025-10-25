package com.ai.robot.controller;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.robot.advisors.TokenCountAdvisor;

@RequestMapping("/api/openai/rag")
@RestController
public class RAGController {

	private ChatClient chatClient;

	private VectorStore vectorStore;

	@Value("classpath:/prompt-template/systemPromptRandomDataTemplate.st")
	private Resource systemPromptRandomDataTemplate;

	public RAGController(OpenAiChatModel openAiChatModel, JdbcChatMemoryRepository jdbcChatMemoryRepository,
			VectorStore vectorStore) {
		ChatOptions chatOptions = OpenAiChatOptions.builder().model(ChatModel.GPT_4_1_NANO).maxCompletionTokens(1000)
				.build();
		MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder()
				.chatMemoryRepository(jdbcChatMemoryRepository).maxMessages(5).build();
		MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(messageWindowChatMemory).build();
		this.chatClient = ChatClient.builder(openAiChatModel).defaultOptions(chatOptions)
				.defaultAdvisors(chatMemoryAdvisor, new TokenCountAdvisor()).build();
		this.vectorStore = vectorStore;
	}

	@GetMapping("/random/chat")
	public ResponseEntity<String> randomChat(@RequestHeader("username") String username,
			@RequestParam("message") String message) { 
		//Query the Vector database to get the similar results.
		SearchRequest search = SearchRequest.builder().query(message).similarityThreshold(0.5).topK(3).build();
		List<Document> similarDocuments = vectorStore.similaritySearch(search);
		String response = chatClient.prompt()
				.user(message)
				.system(t -> t.text(systemPromptRandomDataTemplate).param("documents", similarDocuments))
				.advisors((p) -> p.param(ChatMemory.CONVERSATION_ID, username))
				.call().content();
		return ResponseEntity.ok(response);
	}
}
