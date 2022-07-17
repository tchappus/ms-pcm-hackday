package com.myapp.generator.controller;

import com.myapp.lib.InitiatedPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/send")
    public String send(@RequestBody InitiatedPayment payment) {
        jmsTemplate.convertAndSend("payments-init", payment);

        return "sent";
    }
}
