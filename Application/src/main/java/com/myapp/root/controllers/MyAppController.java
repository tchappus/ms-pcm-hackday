package com.myapp.root.controllers;

import com.myapp.root.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyAppController {
    @Autowired
    private JmsTemplate jmsTemplate;

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @PostMapping("/send")
    public String send(@RequestBody Payment payment) {
        jmsTemplate.convertAndSend("payments", payment);

        return "sent";
    }

    
}
