package com.ai.robot.configuration;

import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdvisorConfiguration {

	@Bean()
	RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore) {
		return RetrievalAugmentationAdvisor.builder()
				.documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).topK(3).similarityThreshold(0.5d).build())
				.build();
	}
}
