package com.ai.robot.advisors;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class TokenCountAdvisor implements CallAdvisor, StreamAdvisor {
	
	private static final String NAME = "TOKEN_COUNT_ADVISOR";
	private final int order;
	
	public TokenCountAdvisor() {
		this.order = 0;
	}
	
	public TokenCountAdvisor(Integer order) {
		this.order = order;
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest,
			StreamAdvisorChain streamAdvisorChain) {
		Flux<ChatClientResponse> chatClientResponses = streamAdvisorChain.nextStream(chatClientRequest);
		return chatClientResponses;
	}

	@Override
	public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
		ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
		// Get the total tokens consumed from the response.
		Integer totalTokenCount = chatClientResponse.chatResponse().getMetadata().getUsage().getTotalTokens();
		log.debug("Total tokens taken by this request is : "+totalTokenCount);
		return chatClientResponse;
	}

	
}
