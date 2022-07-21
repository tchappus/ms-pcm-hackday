package com.myapp.api.controller;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.cosmos.ChangeFeedProcessor;
import com.azure.cosmos.ChangeFeedProcessorBuilder;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.models.ChangeFeedProcessorOptions;
import com.azure.spring.data.cosmos.CosmosFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.lib.EnrichedPayment;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
public class PaymentController {

    private final static Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final CosmosAsyncDatabase db;

    public static ChangeFeedProcessor getChangeFeedProcessor(String hostName, CosmosAsyncContainer feedContainer, CosmosAsyncContainer leaseContainer, Consumer<EnrichedPayment> paymentConsumer) {

        var leasePrefix = String.format("apiSub-%s", UUID.randomUUID());
        var options = new ChangeFeedProcessorOptions();            
        options.setStartFromBeginning(true);
        options.setLeasePrefix(leasePrefix);
        
        return new ChangeFeedProcessorBuilder()
                .hostName(hostName)
                .options(options)
                .feedContainer(feedContainer)
                .leaseContainer(leaseContainer)
                .handleChanges((List<JsonNode> docs) -> {

                    for (JsonNode document : docs) {
                        try {
                            EnrichedPayment payment = OBJECT_MAPPER.treeToValue(document, EnrichedPayment.class);
                            logger.info(leasePrefix + "----=>new payment - id: " + payment.getId());

                            paymentConsumer.accept(payment);
    
                        } catch (JsonProcessingException e) {
                            logger.error("Error processing object", e);
                        }
                    }
                    logger.info("--->handleChanges() END");
    
                })
                .buildChangeFeedProcessor();
    }
 
    public PaymentController(CosmosClientBuilder builder) {
        this.db = CosmosFactory.createCosmosAsyncClient(builder.contentResponseOnWriteEnabled(true)).getDatabase("payments-db");
    }

    @CrossOrigin(allowedHeaders = "*")
    @GetMapping(value = "/payments", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EnrichedPayment> getPayments() {
        return Flux.<EnrichedPayment>create(emitter -> {

            var feedContainer = db.getContainer("payments");
            var leaseContainer = db.getContainer("payments-lease");

            logger.info("-->New subscriber");

            Consumer<EnrichedPayment> paymentConsumer = payment -> {
                emitter.next(payment);
            };

            var changeFeedProcessorInstance = getChangeFeedProcessor("payments-api-host", feedContainer, leaseContainer, paymentConsumer);
            changeFeedProcessorInstance.start()
            .subscribeOn(Schedulers.boundedElastic())
            .doOnSuccess(aVoid -> {
                logger.info("New subscription complete");
            })
            .subscribe();

            emitter.onCancel(() -> changeFeedProcessorInstance.stop());
            emitter.onDispose(() -> changeFeedProcessorInstance.stop());
        });
    }
    
}
