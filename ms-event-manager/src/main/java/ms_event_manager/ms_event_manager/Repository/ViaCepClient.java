package ms_event_manager.ms_event_manager.Repository;

import ms_event_manager.ms_event_manager.Dto.EnderecoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCepClient", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

    @GetMapping("/{cep}/json/")
    EnderecoDTO consultarEndereco(@PathVariable("cep") String cep);
}