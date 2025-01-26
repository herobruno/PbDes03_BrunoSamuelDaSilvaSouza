package ms_ticket_manager.ms_ticket_manager.Service;

import ms_ticket_manager.ms_ticket_manager.Entity.IdCounter;
import ms_ticket_manager.ms_ticket_manager.Repository.IdCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketIdGeneratorService {

    @Autowired
    private IdCounterRepository idCounterRepository;

    // Método para gerar o próximo ID sequencial
    public String generateNextTicketId() {
        // Busca o contador existente
        IdCounter idCounter = idCounterRepository.findById("ticketIdCounter")
                .orElseGet(() -> {
                    // Se o contador não existe, cria um novo com valor 1
                    IdCounter newCounter = new IdCounter();
                    newCounter.setId("ticketIdCounter");
                    newCounter.setCounter(1L);
                    return newCounter;
                });

        // Pega o próximo ID
        Long nextId = idCounter.getCounter();

        // Atualiza o contador
        idCounter.setCounter(nextId + 1);
        idCounterRepository.save(idCounter);

        // Retorna o ID gerado (transformando em String)
        return String.valueOf(nextId);
    }
}