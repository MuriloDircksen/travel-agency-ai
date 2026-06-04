package dev.ia;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("/travel")
public class TravelAgentResource {

    //@Inject
    //TravelAgentAssistant assistant;
    @Inject
    PackageExpert expert;
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
    public String ask(@QueryParam("session") @DefaultValue("") String session, String question) {
        // Usa o id de sessão informado pelo cliente ou gera um novo por requisição,
        // evitando que uma resposta antiga "fixe" todas as respostas seguintes.
        String memoryId = session.isBlank() ? UUID.randomUUID().toString() : session;
        return expert.chat(memoryId, question);
    }
}
