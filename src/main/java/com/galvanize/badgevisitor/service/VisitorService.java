package com.galvanize.badgevisitor.service;

import com.galvanize.badgevisitor.entity.Visitor;
import com.galvanize.badgevisitor.entity.VisitorExtended;
import com.galvanize.badgevisitor.entity.VisitorFronEnd;
import com.galvanize.badgevisitor.repository.VisitorRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Boolean registerVisitor(VisitorFronEnd visitorFronEnd) {
        return false;
    }

    public VisitorFronEnd findByPhoneNumber(String phone) {
        return null;
    }

    public VisitorFronEnd checkout(VisitorFronEnd visitorFronEnd) {
        return null;
    }

    Visitor visitorFromVisitorFrontEnd(VisitorFronEnd visitorFronEnd) {
        return null;
    }

    VisitorExtended visitorExtendedFromVisitorFrontEnd(VisitorFronEnd visitorFronEnd) {
        return null;
    }

    VisitorFronEnd visitorFrontEndFromVisitor(Visitor visitor) {
        return null;
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
