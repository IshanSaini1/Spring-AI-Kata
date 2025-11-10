package com.ai.robot.rag;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class HRPolicyLoader {

	private VectorStore vectorStore;

	@Value("classpath:/Eazybytes_HR_Policies.pdf")
	Resource hrPolicyPdf;

	public HRPolicyLoader(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	@PostConstruct
	public void loadHrPolicyToVectorStore() {
		TikaDocumentReader tReader = new TikaDocumentReader(hrPolicyPdf);
		List<Document> listOfDoc = tReader.get();
		// Split the large document into manageable chunks of data so that token consumption is significantly reduced and searches are faster.
		TextSplitter textSplitter = TokenTextSplitter.builder().withMaxNumChunks(400).withChunkSize(200).build();
		List<Document> result = textSplitter.split(listOfDoc);
		vectorStore.add(result);
	}
}
