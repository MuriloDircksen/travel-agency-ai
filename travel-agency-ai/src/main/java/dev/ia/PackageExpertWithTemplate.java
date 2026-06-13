package dev.ia;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.mcp.runtime.McpToolBox;

public interface PackageExpertWithTemplate {

    @SystemMessage("""
            Você é um assistente virtual da 'Mundo Viagens', um especialista em nossos pacotes de viagem.
            Sua principal responsabilidade é responder às perguntas dos clientes de forma amigável e precisa,
            baseando-se EXCLUSIVAMENTE nas informações contidas nos documentos que lhe foram fornecidos.
            Use APENAS o conteúdo fornecido. NUNCA invente pacotes, preços, datas ou políticas,
            e NUNCA use conhecimento externo ou suposições.
            Se a informação solicitada não estiver presente nos documentos fornecidos, responda
            exatamente e apenas:
            'Desculpe, mas não tenho informações sobre isso. Posso ajudar com mais alguma dúvida sobre os nossos pacotes?'
            """)
    @McpToolBox("booking-server")
    @UserMessage("Do what user is asking {message}. The user used for authnetication is {username}.")
    String chat (@MemoryId String memoryId, String message, String username);
}
