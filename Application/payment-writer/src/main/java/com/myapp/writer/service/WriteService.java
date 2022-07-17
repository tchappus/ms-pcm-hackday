package com.myapp.writer.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.myapp.lib.EnrichedPayment;
import com.myapp.writer.repo.PaymentsRepo;

@Component
public class WriteService {

    private static final Logger logger = LoggerFactory.getLogger(WriteService.class);

    private final PaymentsRepo paymentsRepo;

    public WriteService(PaymentsRepo paymentsRepo) {
        this.paymentsRepo = paymentsRepo;
    }

    @JmsListener(destination = "payments-persist", containerFactory = "jmsListenerContainerFactory")
    public void recieveMessage(EnrichedPayment payment) {
        logger.info(payment.toString());

        paymentsRepo.save(payment).block();
    }
}
