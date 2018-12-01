package com.galvanize.badgevisitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmqpSenderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmqpSenderService.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AmqpSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    void sendMessage(String exchange, String routingKey, Object data) {
        LOGGER.info("Sending message to the queue using routingKey {}. Message= {}", routingKey, data);
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, data);
        } catch (AmqpException e) {
            throw new RuntimeException("RabbitMQ something went wrong", e);
        }
        LOGGER.info("The message has been sent to the queue.");
    }
}
