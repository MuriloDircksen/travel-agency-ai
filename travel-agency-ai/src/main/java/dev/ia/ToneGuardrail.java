package dev.ia;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ToneGuardrail implements OutputGuardrail {

    @Inject
    ToneJudge judge;

    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        String response = aiMessage.text();

        if (response == null || response.isBlank()) {
            return OutputGuardrailResult.success();
        }

        String verdict = judge.isProfessional(response);
        boolean professional = verdict != null && verdict.toLowerCase().contains("true");
        if (!professional) {
            return reprompt("Tom da resposta considerado inadequado.", """
                    Sua resposta foi detectada como rude ou informal demais.
                    Reescreva-a mantendo a polidez e formalidade de um agente de viagens sênior.
                    """);
        }
        return OutputGuardrailResult.success();
    }
}
