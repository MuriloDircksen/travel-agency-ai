package dev.ia;

import dev.langchain4j.guardrail.InputGuardrailException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("/travel")
public class TravelAgentResource {

    //@Inject
    //TravelAgentAssistant assistant;
    @Inject
    PackageExpertWithTemplate expert;

    /*
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    // Com SERVER_SENT_EVENTS há suporte a transmissão em tempo real
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> ask(String question) {
        // Quarkus gerencia o fluxo de tokens
        return assistant.chat(question);
    }
     */

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String ask
            (@QueryParam("session") @DefaultValue("") String session,
             String question, @HeaderParam("X-User-Name") String userName
            ) {
        // Usa o id de sessão informado pelo cliente ou gera um novo por requisição,
        // evitando que uma resposta antiga "fixe" todas as respostas seguintes.
        String memoryId = session.isBlank() ? UUID.randomUUID().toString() : session;
        if (userName != null && !userName.isEmpty()) {
            // Injeta a identidade autenticada na mensagem para que o modelo possa
            // repassá-la às ferramentas de reserva (ex.: autorização do cancelamento).
            String authenticatedQuestion = "[Usuário autenticado: " + userName + "]\n" + question;
            try {
                String answer = expert.chat(memoryId, authenticatedQuestion, userName);
                return (answer == null || answer.isBlank())
                        ? "Desculpe, não consegui processar sua solicitação. Tente novamente."
                        : answer;
            } catch (InputGuardrailException e) {
                return "Sua mensagem foi bloqueada por conter instruções não permitidas.";
            }
        } else {
            return "Usuário precisa estar autenticado";
        }
    }
}
