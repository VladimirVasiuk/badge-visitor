package com.galvanize.badgevisitor.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.galvanize.badgevisitor.entity.VisitorFrontEnd;
import com.galvanize.badgevisitor.exception.VisitorCannotCheckoutException;
import com.galvanize.badgevisitor.exception.VisitorNotCreatedException;
import com.galvanize.badgevisitor.exception.VisitorNotFoundException;
import com.galvanize.badgevisitor.service.AmqpSenderService;
import com.galvanize.badgevisitor.service.VisitorService;
import org.apache.juli.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.print.attribute.standard.Media;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = VisitorController.class, secure = false)
public class VisitorControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorControllerTest.class);

    @MockBean
    VisitorService service;
    @MockBean
    AmqpSenderService senderService;
    @Autowired
    MockMvc mockMvc;


    @Test
    public void registerVisitorTest() throws Exception {
        VisitorFrontEnd mockVisitor = VisitorFrontEnd.builder().build();
        LOGGER.info("Create mockVisitor: {}", mockVisitor);

        when(service.registerVisitor(mockVisitor)).thenReturn(true);

        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                .post("/visitor/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(mockVisitor))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(service).registerVisitor(any(VisitorFrontEnd.class));
    }

    @Test
    public void registerVisitorWithoutRabbitMQTest() throws Exception {
        VisitorFrontEnd mockVisitor = new VisitorFrontEnd();
        when(service.registerVisitor(mockVisitor)).thenThrow(VisitorNotCreatedException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/visitor/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsBytes(mockVisitor))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isExpectationFailed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(service).registerVisitor(any(VisitorFrontEnd.class));
    }

    @Test
    public void lookupVisitorByPhoneTest() throws Exception {
        String mockPhoneNumber = "mockNumber";

        when(service.findByPhoneNumber(mockPhoneNumber)).thenReturn(new VisitorFrontEnd());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/visitor/lookup/{phoneNumber}", mockPhoneNumber)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(service).findByPhoneNumber(anyString());
    }

    @Test
    public void lookupVisitorByWrongPhoneTest() throws Exception {
        String mockPhoneNumber = "mockNumber";

        when(service.findByPhoneNumber(mockPhoneNumber)).thenThrow(VisitorNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/visitor/lookup/{phoneNumber}", mockPhoneNumber)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(service).findByPhoneNumber(anyString());
    }

    @Test
    public void checkoutTest() throws Exception {
        VisitorFrontEnd visitorFrontEnd = VisitorFrontEnd.builder().build();

        when(service.checkout(visitorFrontEnd)).thenReturn(visitorFrontEnd);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/visitor/checkout")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsBytes(visitorFrontEnd))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();

        verify(service).checkout(any(VisitorFrontEnd.class));
    }

    @Test
    public void checkoutWithoutRabbitMQTest() throws Exception {
        VisitorFrontEnd visitorFrontEnd = VisitorFrontEnd.builder().build();

        when(service.checkout(visitorFrontEnd)).thenThrow(VisitorCannotCheckoutException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/visitor/checkout")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsBytes(visitorFrontEnd))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isExpectationFailed())
                .andDo(print())
                .andReturn();

        verify(service).checkout(any(VisitorFrontEnd.class));
    }
}