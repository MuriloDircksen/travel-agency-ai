package dev.ia;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;

@ApplicationScoped
public class JsonStructGuard implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        String response = aiMessage.text();

        if (response == null || response.isBlank()) {
            return OutputGuardrailResult.success();
        }

        try (JsonReader reader = Json.createReader(new StringReader(response))){
            // Tenta fazer o parse, se falahar, o json é inválido.
            JsonObject jsonObject = reader.readObject();
            return OutputGuardrailResult.success();
        } catch (Exception e) {
            return reprompt("Resposta não é um JSON válido: " + e.getMessage(), """
                    Erro: Sua resposta não é um JSON válido.
                    Gere NOVAMENTE apenas o JSON, sem blocos de código markdown ou texto adicional.
                    """);
        }
    }
}
