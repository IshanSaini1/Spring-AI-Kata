package com.ai.robot.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/openai/audio")
public class AudioController {

	private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
	
	private final OpenAiAudioSpeechModel openAiAudioSpeechModel;
	
	@Value("classpath:/audio/spring-ai-mono.wav")
	Resource springAiSpeech;

	public AudioController(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel, OpenAiAudioSpeechModel openAiAudioSpeechModel) {
		this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
		this.openAiAudioSpeechModel = openAiAudioSpeechModel;
	}
	
	@GetMapping("/transcribe")
	String transcribe() {
		String textFromSpeech = openAiAudioTranscriptionModel.call(springAiSpeech);
		return textFromSpeech;
	}
	
	@GetMapping("/speech")
	String speech(@RequestParam("message") String speechText) throws IOException {
		byte[] audioBytes = openAiAudioSpeechModel.call(speechText);
		Path path = Paths.get("output.mp3");
		Files.write(path, audioBytes);
		return "mp3 saved sucessfully";
	}

}
