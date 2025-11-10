package com.ai.robot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ai.robot.configuration.TavilyConfigurations;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor

@Service
@Slf4j
public class TavilyService {
	
	@Autowired
	TavilyConfigurations tavilyConfigurations;

	public static final String TOPIC = "general";
	public static final String SEARCH_DEPTH = "basic";
	public static final int MAX_RESULTS = 3;
	public static final boolean INCLUDE_ANSWER = true;
	public static final boolean INCLUDE_RAW_CONTENT = true;

	public ResponseEntity<TavilyResponse> triggerTavilyAPI(TavilyRequest request) {

		RestClient client = RestClient.builder().baseUrl(tavilyConfigurations.getHostname())
				.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tavilyConfigurations.getKey())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

		ResponseEntity<TavilyResponse> response = client.post().uri("/search").body(request).retrieve()
				.toEntity(TavilyResponse.class);

		return response;
	}

	@Data
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class TavilyRequest {
		@JsonProperty("query")
		private String prompt;
		@JsonProperty("topic")
		private String topic;
		@JsonProperty("search_depth")
		private String searchDepth;
		@JsonProperty("max_results")
		private int maxResults;
		@JsonProperty("include_answer")
		private boolean includeAnswer;
		@JsonProperty("include_raw_content")
		private boolean includeRawContent;

		public TavilyRequest(String prompt) {
			this(prompt, SEARCH_DEPTH, MAX_RESULTS, INCLUDE_ANSWER, INCLUDE_RAW_CONTENT, TOPIC);
		}

		public TavilyRequest(String prompt, boolean includeAnswer) {
			this(prompt, SEARCH_DEPTH, MAX_RESULTS, includeAnswer, INCLUDE_RAW_CONTENT, TOPIC);
		}

		public TavilyRequest(String prompt, String searchDepth, Integer maxResults, boolean includeAnswer,
				boolean includeRawContent, String topic) {
			this.prompt = prompt;
			this.searchDepth = searchDepth;
			this.maxResults = maxResults;
			this.includeAnswer = includeAnswer;
			this.includeRawContent = includeRawContent;
			this.topic = topic;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class TavilyResponse {

		private String query;
		private List<String> followUpQuestions;
		private String answer;
		private List<String> images;
		private List<Result> results;
		private double responseTime;
		private String requestId;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		@JsonInclude(JsonInclude.Include.NON_NULL)
		@JsonIgnoreProperties(ignoreUnknown = true)
		@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
		public static class Result {
			private String title;
			private String url;
			private String content;
			private double score;
			private String rawContent;
		}
	}
}
