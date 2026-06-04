package dev.ia;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class RagConfiguration {

    @Produces
    public RetrievalAugmentor retrievalAugmentor(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        return DefaultRetrievalAugmentor.builder()
                .contentRetriever(EmbeddingStoreContentRetriever.builder()
                        .embeddingStore(embeddingStore)
                        .embeddingModel(embeddingModel)
                        .maxResults(3) //top 3 segmentos no banco de vetores
                        // Sem um limite de relevância, consultas fora do contexto ainda
                        // recuperariam o catálogo inteiro e o modelo nunca recusaria.
                        // Ajuste entre 0.5 e 0.75 conforme a qualidade da recuperação.
                        .minScore(0.6)
                        .build()
                ).build();
    }
}
