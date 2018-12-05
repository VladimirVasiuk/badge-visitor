package com.galvanize.badgevisitor.service;

import com.galvanize.badgevisitor.entity.Visitor;
import com.galvanize.badgevisitor.entity.VisitorFrontEnd;
import com.galvanize.badgevisitor.exception.VisitorCannotCheckoutException;
import com.galvanize.badgevisitor.exception.VisitorNotCreatedException;
import com.galvanize.badgevisitor.exception.VisitorNotFoundException;
import com.galvanize.badgevisitor.repository.VisitorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VisitorServiceTest {

    @MockBean
    VisitorRepository repository;
    @MockBean
    AmqpSenderService amqpSenderService;
    @Autowired
    VisitorService service;

    @Test(expected = IllegalArgumentException.class)
    public void registerVisitorPhoneNullTest() {
        VisitorFrontEnd mockVisitorFrontEnd = VisitorFrontEnd.builder().build();
        Visitor mockVisitor = service.visitorFromVisitorFrontEnd(mockVisitorFrontEnd);
        when(repository.save(mockVisitor)).thenReturn(mockVisitor);
        doNothing().when(amqpSenderService)
                .sendMessage(service.getExchangeName(), service.getVerifyRoutingKey(), mockVisitorFrontEnd);
        service.registerVisitor(mockVisitorFrontEnd);
        verify(repository, times(1)).save(mockVisitor);
        verify(amqpSenderService, times(1))
                .sendMessage(service.getExchangeName(), service.getVerifyRoutingKey(), mockVisitorFrontEnd);
    }

    @Test
    public void registerVisitor() {
        VisitorFrontEnd mockVisitorFrontEnd = VisitorFrontEnd.builder().phoneNumber("(555)111-2233").build();
        Visitor mockVisitor = service.visitorFromVisitorFrontEnd(mockVisitorFrontEnd);
        when(repository.save(mockVisitor)).thenReturn(mockVisitor);
        doNothing().when(amqpSenderService)
                .sendMessage(service.getExchangeName(), service.getVerifyRoutingKey(), mockVisitorFrontEnd);
        service.registerVisitor(mockVisitorFrontEnd);
        verify(repository, times(1)).save(mockVisitor);
        verify(amqpSenderService, times(1))
                .sendMessage(service.getExchangeName(), service.getVerifyRoutingKey(), mockVisitorFrontEnd);
    }

    @Test(expected = VisitorNotCreatedException.class)
    public void registerVisitorWithoutRabbitMQTest() {
        VisitorFrontEnd mockVisitorFrontEnd = VisitorFrontEnd.builder().phoneNumber("(555)111-2233").build();
        Visitor mockVisitor = service.visitorFromVisitorFrontEnd(mockVisitorFrontEnd);
        when(repository.save(mockVisitor)).thenReturn(mockVisitor);
        doThrow(RuntimeException.class).when(amqpSenderService)
                .sendMessage(service.getExchangeName(), service.getVerifyRoutingKey(), mockVisitorFrontEnd);
        service.registerVisitor(mockVisitorFrontEnd);
        verify(repository, times(1)).save(mockVisitor);
        verify(amqpSenderService, times(1))
                .sendMessage(service.getExchangeName(), service.getVerifyRoutingKey(), mockVisitorFrontEnd);
    }

    @Test
    public void findByPhoneNumberTest() {
        String mockStringPhoneNumber = "(555)111-2233";
        Long mockPhoneNumber = 5551112233L;
        Visitor mockVisitor = Visitor.builder().build();
        when(repository.findById(mockPhoneNumber)).thenReturn(Optional.of(mockVisitor));
        service.findByPhoneNumber(mockStringPhoneNumber);
        verify(repository, times(1)).findById(mockPhoneNumber);
    }

    @Test(expected = VisitorNotFoundException.class)
    public void findByWrongPhoneNumberTest() {
        String mockStringPhoneNumber = "";
        Long mockPhoneNumber = anyLong();
        when(repository.findById(mockPhoneNumber)).thenReturn(Optional.empty());
        service.findByPhoneNumber(mockStringPhoneNumber);
        verify(repository, times(1)).findById(mockPhoneNumber);
    }

    @Test
    public void checkoutTest() {
        VisitorFrontEnd mockVisitorFrontEnd = VisitorFrontEnd.builder().phoneNumber("(555)111-2233").build();
        doNothing().when(amqpSenderService)
                .sendMessage(service.getExchangeName(), service.getCheckoutRoutingKey(), mockVisitorFrontEnd);
        service.checkout(mockVisitorFrontEnd);
        verify(amqpSenderService, times(1))
                .sendMessage(service.getExchangeName(), service.getCheckoutRoutingKey(), mockVisitorFrontEnd);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkoutPhoneNullTest() {
        VisitorFrontEnd mockVisitorFrontEnd = VisitorFrontEnd.builder().build();
        doNothing().when(amqpSenderService)
                .sendMessage(service.getExchangeName(), service.getCheckoutRoutingKey(), mockVisitorFrontEnd);
        service.checkout(mockVisitorFrontEnd);
        verify(amqpSenderService, times(1))
                .sendMessage(service.getExchangeName(), service.getCheckoutRoutingKey(), mockVisitorFrontEnd);
    }

    @Test(expected = VisitorCannotCheckoutException.class)
    public void checkoutWithoutRabbitMQTest() {
        VisitorFrontEnd mockVisitorFrontEnd = VisitorFrontEnd.builder().phoneNumber("(555)111-2233").build();
        doThrow(RuntimeException.class).when(amqpSenderService)
                .sendMessage(service.getExchangeName(), service.getCheckoutRoutingKey(), mockVisitorFrontEnd);
        service.checkout(mockVisitorFrontEnd);
        verify(amqpSenderService, times(1))
                .sendMessage(service.getExchangeName(), service.getCheckoutRoutingKey(), mockVisitorFrontEnd);
    }
}