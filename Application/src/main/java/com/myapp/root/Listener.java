package com.myapp.root;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Component
public class Listener {

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private PaymentsRepo paymentsRepo;
    private BicCodeRepo bicCodeRepo;

    private String readBicTextFile() throws IOException {
        StringBuilder builder = new StringBuilder();
        try (
                InputStream inputStream = Listener.class.getClassLoader().getResourceAsStream("US-BIC.json");
                InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader);
        ) {
            for (String line; (line = reader.readLine()) != null; ) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    public Listener(PaymentsRepo paymentsRepo, BicCodeRepo bicCodeRepo) throws IOException {
        this.paymentsRepo = paymentsRepo;
        this.bicCodeRepo = bicCodeRepo;

        String text = readBicTextFile();

        BicCodeList bics = MAPPER.readValue(text, BicCodeList.class);
        logger.info("BIC codes found {}", bics.getList().size());

        logger.info("Initializing cache...");
        bics.getList().parallelStream().forEach(b -> {
            if (Boolean.FALSE.equals(bicCodeRepo.existsById(b.getSwift_code()))) {
                logger.info("Adding key = {}", b.getSwift_code());
                bicCodeRepo.save(b);
            } else {
                logger.info("Already has key = {}", b.getSwift_code());
            }
        });
        logger.info("Initialization finished.");
    }

    @JmsListener(destination = "payments", containerFactory = "jmsListenerContainerFactory")
    public void recieveMessage(InitiatedPayment payment) {
        logger.info(payment.toString());
        EnrichedPayment enrichedPayment = new EnrichedPayment(payment);
        String bic = payment.getBicCode();

        Optional<BicCode> bicFromRedis = bicCodeRepo.findById(bic);

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

        paymentsRepo.save(enrichedPayment).block();
    }
}
