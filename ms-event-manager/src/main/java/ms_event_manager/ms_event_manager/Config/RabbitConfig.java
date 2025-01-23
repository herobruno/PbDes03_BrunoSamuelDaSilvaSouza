package ms_event_manager.ms_event_manager.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {


    @Bean
    public Queue eventQueue() {
        return new Queue("eventQueue", false);
    }
}