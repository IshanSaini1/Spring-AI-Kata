package com.ai.robot.rag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.stereotype.Component;

import com.ai.robot.service.TavilyService;

import lombok.Builder;

@Component
@Builder
public class WebSearchDocumentRetriever implements DocumentRetriever {

	private TavilyService tavilyService;
	
	public WebSearchDocumentRetriever(TavilyService tavilyService) {
		this.tavilyService = tavilyService;
	}
	
	@Override
	public List<Document> retrieve(Query query){
		String message = query.text();
		if(message == null || message.isEmpty()) {
			return List.of();
		}
		TavilyService.TavilyRequest tavilyRequest = new TavilyService.TavilyRequest(message);
		var rawResponse = tavilyService.triggerTavilyAPI(tavilyRequest);
		var tavilyResponseObject = rawResponse.getBody();
		List<Document> lod = new ArrayList<>();
		tavilyResponseObject.getResults().stream().forEach(x->{
			Document doc = Document.builder()
					.text(x.getContent())
					.metadata("title", x.getTitle())
					.metadata("url", x.getUrl())
					.score(x.getScore())
					.build();
			lod.add(doc);
		});
		return lod;
	}

}
