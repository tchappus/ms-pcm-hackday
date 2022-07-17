package com.myapp.enricher.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.enricher.model.BicCodeList;
import com.myapp.enricher.repo.BicCodeRepo;
import com.myapp.lib.EnrichedPayment;
import com.myapp.lib.InitiatedPayment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class BicInfoService {

    private static final Logger logger = LoggerFactory.getLogger(BicInfoService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final BicCodeRepo bicCodeRepo;
    private final JmsTemplate jmsTemplate;

    private String readBicTextFile() throws IOException {
        var builder = new StringBuilder();
        try (
                var inputStream = BicInfoService.class.getClassLoader().getResourceAsStream("US-BIC.json");
                var streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                var reader = new BufferedReader(streamReader);
        ) {
            for (String line; (line = reader.readLine()) != null; ) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    public BicInfoService(BicCodeRepo bicCodeRepo, JmsTemplate jmsTemplate) throws IOException {
        this.bicCodeRepo = bicCodeRepo;
        this.jmsTemplate = jmsTemplate;

        var text = readBicTextFile();

        var bics = MAPPER.readValue(text, BicCodeList.class);
        logger.info("BIC codes found {}", bics.getList().size());

        logger.info("Initializing cache...");
        bics.getList().parallelStream().forEach(b -> {
            if (bicCodeRepo.existsById(b.getSwift_code()) == false) {
                logger.info("Adding key = {}", b.getSwift_code());
                bicCodeRepo.save(b);
            } else {
                logger.info("Already has key = {}", b.getSwift_code());
            }
        });
        logger.info("Initialization finished.");
    }

    @JmsListener(destination = "payments-init", containerFactory = "jmsListenerContainerFactory")
    public void recieveMessage(InitiatedPayment payment) {
        logger.info(payment.toString());
        var enrichedPayment = new EnrichedPayment(payment);
        var bic = payment.getBicCode();

        var bicFromRedis = bicCodeRepo.findById(bic);

        if (bicFromRedis.isPresent()) {
            enrichedPayment.setCity(bicFromRedis.get().getCity());
            enrichedPayment.setBankName(bicFromRedis.get().getBank());
            enrichedPayment.setBranch(bicFromRedis.get().getBranch());
        } else {
            enrichedPayment.setCity("unknown");
            enrichedPayment.setBankName("unknown");
            enrichedPayment.setBranch("unknown");
        }

        enrichedPayment.setId(String.valueOf(UUID.randomUUID()));

        jmsTemplate.convertAndSend("payments-persist", enrichedPayment);
    }
}
