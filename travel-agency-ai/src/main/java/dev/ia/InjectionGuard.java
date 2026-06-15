package dev.ia;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InjectionGuard implements InputGuardrail {
    @Inject
    PromptSecurityExpert securityExpert;

    @Override
    public InputGuardrailResult validate(UserMessage userMesssage) {
        // O modelo nem sempre devolve apenas "true"/"false" — pode acrescentar
        // explicações. Interpretamos a resposta em Java em vez de confiar no
        // parser estrito de Boolean (que lançaria OutputParsingException).
        String verdict = securityExpert.isAttack(userMesssage.singleText());
        if (verdict != null && verdict.toLowerCase().contains("true")) {
            return failure("Sua mensagem foi bloqueada por conter instruções não permitidas.");
        }
        return InputGuardrailResult.success();
    }
}
