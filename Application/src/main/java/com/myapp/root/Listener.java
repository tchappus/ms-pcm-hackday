package com.myapp.root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);

    @JmsListener(destination = "payments", containerFactory = "jmsListenerContainerFactory")
    public void recieveMessage(Payment payment) {
        logger.debug(payment.toString());
    }
}
