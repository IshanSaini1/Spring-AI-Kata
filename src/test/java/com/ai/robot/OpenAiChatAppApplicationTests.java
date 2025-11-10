package com.ai.robot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Timeout;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.ai.robot.controller.OpenAIController;
import com.ai.robot.controller.PromptStuffingController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(properties = { "spring.ai.openai.api-key=${OPEN_AI_KEY}", "logging.level.com.ai.robot=debug",
		"logging.level.org.springframework.ai.chat.client.advisor=debug" })
class OpenAiChatAppApplicationTests {

	@Autowired
	private OpenAIController openAIController;
	@Autowired
	private PromptStuffingController promptStuffingController;
	@Autowired
	private OpenAiChatModel openAiChatModel;

	private ChatClient chatClient;

	private RelevancyEvaluator relevancyEvaluator;

	@BeforeAll
	void setup() {
		ChatOptions options = OpenAiChatOptions.builder().model(ChatModel.GPT_4_1).maxCompletionTokens(5000).build();
		this.chatClient = ChatClient.builder(openAiChatModel).defaultAdvisors(new SimpleLoggerAdvisor())
				.defaultOptions(options).build();
		this.relevancyEvaluator = new RelevancyEvaluator(chatClient.mutate());
	}

	@Test
	@DisplayName("Should return relevant response for basic geography questions")
	@Timeout(value = 30, unit = TimeUnit.SECONDS)
	void evaluateChatControllerResponseRelevancy() {
		// Given
		String question = "What is the capital of India ?";

		// When
		String aiModelResponse = openAIController.openAiChat(question);

		// Then
		EvaluationRequest evaluationRequest = new EvaluationRequest(question, aiModelResponse);
		EvaluationResponse response = relevancyEvaluator.evaluate(evaluationRequest);
		log.debug("{} is the relevancy score.", response.getScore());
		Assertions.assertAll(() -> assertThat(aiModelResponse).isNotBlank(),
				() -> assertThat(response.isPass()).withFailMessage("""
						The Answer is not relevant.
						""").isTrue());
	}

}
