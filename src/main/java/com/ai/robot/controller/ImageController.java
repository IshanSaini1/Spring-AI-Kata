package com.ai.robot.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.ai.image.ImageMessage;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/openai/image")
public class ImageController {
	
	OpenAiImageModel openAiImageModel;
	
	public ImageController(OpenAiImageModel openAiImageModel) {
		this.openAiImageModel = openAiImageModel;
	}
	
	@GetMapping("/generate")
	String genarateImage(@RequestParam("message") String message) throws IOException {
		ImageMessage imageMessage = new ImageMessage(message) ;
		ImageOptions imageOptions = ImageOptionsBuilder.builder().style("vivid").responseFormat("url").build();
		ImageResponse generatedImageResponse = openAiImageModel.call(new ImagePrompt(imageMessage, imageOptions));
		return generatedImageResponse.getResult().getOutput().getUrl();
	}
}
