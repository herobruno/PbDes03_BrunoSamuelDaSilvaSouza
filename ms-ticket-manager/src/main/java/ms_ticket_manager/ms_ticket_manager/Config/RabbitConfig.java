package ms_ticket_manager.ms_ticket_manager.Config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Exchange eventExchange() {
        return ExchangeBuilder.directExchange("event-exchange").durable(true).build();
    }

    @Bean
    public Queue eventQueue() {
        return new Queue("event.create.ticket", true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(eventQueue()).to(eventExchange()).with("event.create.ticket").noargs();
    }
}