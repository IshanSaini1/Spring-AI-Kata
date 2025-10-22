package com.ai.robot.controller;

import org.springframework.ai.bedrock.converse.BedrockProxyChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aws")
public class AwsAIController {

	private ChatClient bedrockClient;

	public AwsAIController(BedrockProxyChatModel bedrockProxyChatModel) {
		this.bedrockClient = ChatClient.builder(bedrockProxyChatModel).build();
	}

	@GetMapping("/chat")
	public String getBedrockResponse(@RequestParam("message") String messsage) {
		return bedrockClient.prompt(messsage).call().content();
	}

}
