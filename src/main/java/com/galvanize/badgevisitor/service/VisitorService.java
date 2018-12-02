package com.galvanize.badgevisitor.service;

import com.galvanize.badgevisitor.entity.Visitor;
import com.galvanize.badgevisitor.entity.VisitorExtended;
import com.galvanize.badgevisitor.entity.VisitorFrontEnd;
import com.galvanize.badgevisitor.exception.VisitorCannotCheckoutException;
import com.galvanize.badgevisitor.exception.VisitorNotCreatedException;
import com.galvanize.badgevisitor.exception.VisitorNotFoundException;
import com.galvanize.badgevisitor.repository.VisitorRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Data
@Service
public class VisitorService {

    private final static Logger LOGGER = LoggerFactory.getLogger(VisitorService.class);

    private final VisitorRepository repository;
    private final AmqpSenderService senderService;
    @Value("${amqp.exchange.name}")
    String exchangeName;
    @Value("${amqp.verify.routing.key}")
    String verifyRoutingKey;
    @Value("${amqp.checkout.routing.key}")
    String checkoutRoutingKey;

    @Autowired
    public VisitorService(VisitorRepository repository, AmqpSenderService senderService) {
        this.repository = repository;
        this.senderService = senderService;
    }


    @Transactional
    public Boolean registerVisitor(VisitorFrontEnd visitorFrontEnd) {
        Long phoneNumber = phoneNumberFromString(visitorFrontEnd.getPhoneNumber());
        if (phoneNumber == null)
            throw new IllegalArgumentException("Phone number is null");
        VisitorExtended visitorExtended = visitorExtendedFromVisitorFrontEnd(visitorFrontEnd);
        Visitor visitor = visitorFromVisitorFrontEnd(visitorFrontEnd);

        repository.save(visitor);
        try {
            senderService.sendMessage(exchangeName, verifyRoutingKey, visitorExtended);
        } catch (RuntimeException e) {
            LOGGER.error("Visitor wasn't create. RabbitMQ wrong.");
            throw new VisitorNotCreatedException("Visitor wasn't create. RabbitMQ wrong.");
        }
        return true;
    }

    public VisitorFrontEnd findByPhoneNumber(String phone) {
        if (phone == null)
            throw new IllegalArgumentException("Phone number is null");
        Optional<Visitor> optVisitor = repository.findById(phoneNumberFromString(phone));
        if (!optVisitor.isPresent())
            throw new VisitorNotFoundException(String.format("Visitor wasn't found by phone: %s", phone));
        return visitorFrontEndFromVisitor(optVisitor.get());
    }

    public VisitorFrontEnd checkout(VisitorFrontEnd visitorFrontEnd) {
        Long phoneNumber = phoneNumberFromString(visitorFrontEnd.getPhoneNumber());
        if (phoneNumber == null)
            throw new IllegalArgumentException("Phone number is null");
        VisitorExtended visitorExtended = visitorExtendedFromVisitorFrontEnd(visitorFrontEnd);
        try {
            senderService.sendMessage(exchangeName, checkoutRoutingKey, visitorExtended);
        } catch (RuntimeException e) {
            LOGGER.error("Visitor wasn't create. RabbitMQ wrong.");
            throw new VisitorCannotCheckoutException("Visitor cannot checkout. RabbitMQ wrong.");
        }
        return visitorFrontEnd;
    }

    Visitor visitorFromVisitorFrontEnd(VisitorFrontEnd visitorFrontEnd) {
        return Visitor.builder()
                .phoneNumber(phoneNumberFromString(visitorFrontEnd.getPhoneNumber()))
                .firstName(visitorFrontEnd.getFirstName())
                .lastName(visitorFrontEnd.getLastName())
                .company(visitorFrontEnd.getCompany())
                .build();
    }

    VisitorExtended visitorExtendedFromVisitorFrontEnd(VisitorFrontEnd visitorFrontEnd) {
        return VisitorExtended.builder()
                .phoneNumber(phoneNumberFromString(visitorFrontEnd.getPhoneNumber()))
                .firstName(visitorFrontEnd.getFirstName())
                .lastName(visitorFrontEnd.getLastName())
                .company(visitorFrontEnd.getCompany())
                .hostName(visitorFrontEnd.getHostName())
                .hostPhone(phoneNumberFromString(visitorFrontEnd.getHostPhone()))
                .purposeOfVisit(visitorFrontEnd.getPurposeOfVisit())
                .build();
    }

    VisitorFrontEnd visitorFrontEndFromVisitor(Visitor visitor) {
        return VisitorFrontEnd.builder()
                .phoneNumber(phoneNumberStringFormat(visitor.getPhoneNumber()))
                .firstName(visitor.getFirstName())
                .lastName(visitor.getLastName())
                .company(visitor.getCompany())
                .build();
    }


    private Long phoneNumberFromString(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty())
            return null;
        return Long.parseLong(phoneNumber.replaceAll("[^0-9]", ""));
    }

    private String phoneNumberStringFormat(Long number) {
        if (number == null || number == 0)
            return "";
        String phone = number.toString();
        try {
            return String.format("(%s)%s-%s",
                    phone.substring(0, 3),
                    phone.substring(3, 6),
                    phone.substring(6));
        } catch (Exception e) {
            return phone;
        }
    }
}
