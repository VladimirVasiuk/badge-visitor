package com.galvanize.badgevisitor.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqpSenderServiceTest {

    @MockBean
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpSenderService amqpSenderService;

    private String mockExchange = "";
    private String mockRoutingKey = "";
    private String mockMessageData = "";

    @Test
    public void sendMessageTest() {
        doNothing().when(rabbitTemplate)
                .convertAndSend(mockExchange, mockRoutingKey, mockMessageData);
        amqpSenderService.sendMessage(mockExchange, mockRoutingKey, mockMessageData);
        verify(rabbitTemplate, times(1))
                .convertAndSend(mockExchange, mockRoutingKey, mockMessageData);
    }

    @Test(expected = RuntimeException.class)
    public void sendMessageWrongRabbitMQTest() {
        doThrow(RuntimeException.class).when(rabbitTemplate)
                .convertAndSend(mockExchange, mockRoutingKey, mockMessageData);
        amqpSenderService.sendMessage(mockExchange, mockRoutingKey, mockMessageData);
        verify(rabbitTemplate, times(1))
                .convertAndSend(mockExchange, mockRoutingKey, mockMessageData);
    }
}