package com.galvanize.badgevisitor.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfigAmqpUri {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfigAmqpUri.class);

    @Value("${amqp.uri}")
    String amqpUri;

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        LOGGER.info("Create RabbitMQ Template Bean with own connection factory");
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        LOGGER.info("Create producerJackson2MessageConverter bean");
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    ConnectionFactory connectionFactory() {
        LOGGER.info("Create Connection factory Bean with owm AMQP URI: {}", amqpUri);
        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(amqpUri);
        return factory;
    }


}
