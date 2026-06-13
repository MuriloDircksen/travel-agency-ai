package dev.ia.travel.tools;

import dev.ia.travel.BookingService;
import dev.ia.travel.enums.Category;
import dev.ia.travel.models.Booking;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BookingTools {

    @Inject
    BookingService bookingService;

    @Tool(name="getBookingDetails",
            description="Obtém os detalhes completos de uma reserva com base em seu número de identificação (bookingId).")
    public String getBookingDetails(
            @ToolArg(description="O ID numérico único da reserva (ex: 12345)") long bookingId) {
        return bookingService.getBookingDetails(bookingId)
                .map(Booking::toString)
                .orElse("Reserva com ID" + bookingId + " não encontrada.");
    }

    @Tool(name="cancelBooking",
            description="""
            Cancela uma reserva existente.
            Para confirmar o cancelamento, é necessário fornecer o ID da reserva (bookingId).
            O usuário deve estar autenticado.
            """)
    public String cancelBooking(
            @ToolArg(description="O ID numérico único da reserva (ex: 12345)") long bookingId,
            @ToolArg(description="Usuário que está tentando cancelar a reserva") String name) {
        return bookingService.cancelBooking(bookingId, name)
                .map(booking -> "Reserva " + bookingId + " cancelada com sucesso. Status atual: " + booking.status())
                .orElse("Não foi possível cancelar a reserva. Verifique se o ID da reserva esta correto ou se você tem permissão");
    }

    @Tool(name="listPackagesByCategory",
            description="Lista os pacotes de viagem disponíveis para uma determinada categoria (ex: ADVENTURE, TREASURES).")
    public String listPackagesByCategory(
            @ToolArg(description="Categoria utilizada como filtro de pacotes")Category category) {
        List<Booking> packages = bookingService.findPackagesByCategory(category);

        if(packages.isEmpty()) {
            return "Nenhum pacote encontrada para a categoria '" + category +"': " + packages;
        }
        return "Pacotes encontrados para a categoria '" + category + "': " + packages.stream()
                .map(Booking::destination)
                .toList().toString();
    }
}
