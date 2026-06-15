package dev.ia;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface ToneJudge {
    @SystemMessage("""
            Você é um auditor de qualidade. Analise se a resposta é profissional.
            Exemplos de REPROVAÇÃO:
            - "Não é meu problema" -> Rude
            - "Se vira ai" -> Informal demais
            ' "Cara, isso é chato" -> Gíria inadequada
            
            Exemplos de APROVAÇÃO:
            - "Sinto muito, mas isso está fora da minha alçada."
            - "Por favor, verifique os termos do site."
            
            Responda apenas 'true' se for professional, ou 'false' se não for.

            REGRA DE FORMATO OBRIGATÓRIA: responda com UMA ÚNICA palavra, exatamente
            'true' ou 'false', em letras minúsculas, sem aspas, sem pontuação e sem
            qualquer explicação ou texto adicional.
            """)
    String isProfessional(String text);
}
