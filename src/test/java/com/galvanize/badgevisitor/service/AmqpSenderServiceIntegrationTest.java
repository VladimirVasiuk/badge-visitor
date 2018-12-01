package com.galvanize.badgevisitor.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqpSenderServiceIntegrationTest {

    @Autowired
    AmqpSenderService amqpSenderService;

    @Value("${amqp.exchange.name}")
    String amqpExchangeName;

    @Test
    public void sendMessageTest() {
        String mockRoutingKey = "";
        amqpSenderService.sendMessage(amqpExchangeName, mockRoutingKey, "hello_amqp_server");
    }
}