package dev.ia;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.InputStream;
import java.util.List;

@ApplicationScoped
public class DocumentIngestor {

    private static final Logger LOG = Logger.getLogger(DocumentIngestor.class);

    // Carregado do classpath (src/main/resources) para não depender do diretório de execução.
    private static final String RAG_RESOURCE = "rag/pacotes-viagem.md";

    @Inject
    EmbeddingStore<TextSegment> store;

    @Inject
    EmbeddingModel embeddingModel;

    public void onStart(@Observes StartupEvent event) {
        Document document = loadFromClasspath(RAG_RESOURCE);
        document.metadata().put("type", "packages");

        DocumentSplitter splitter = DocumentSplitters.recursive(200, 20);

        // Conta os segmentos para tornar o sucesso da ingestão observável no log.
        List<TextSegment> segments = splitter.split(document);
        LOG.infof("RAG: ingerindo '%s' (%d caracteres) em %d segmento(s)",
                RAG_RESOURCE, document.text().length(), segments.size());

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(store)
                .build();

        ingestor.ingest(document);
        LOG.infof("RAG: ingestão concluída para '%s'", RAG_RESOURCE);
    }

    private Document loadFromClasspath(String resourcePath) {
        DocumentParser parser = new TextDocumentParser();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalStateException(
                        "Documento RAG não encontrado no classpath: " + resourcePath);
            }
            return parser.parse(is);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao carregar documento RAG: " + resourcePath, e);
        }
    }
}
