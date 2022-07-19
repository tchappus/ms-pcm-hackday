package com.myapp.api.controller;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.cosmos.ChangeFeedProcessor;
import com.azure.cosmos.ChangeFeedProcessorBuilder;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClient;
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

        var options = new ChangeFeedProcessorOptions();            
        options.setStartFromBeginning(false);
        options.setLeasePrefix("myChangeFeedDeploymentUnit");
        
        return new ChangeFeedProcessorBuilder()
                .hostName(hostName)
                .options(options)
                .feedContainer(feedContainer)
                .leaseContainer(leaseContainer)
                .handleChanges((List<JsonNode> docs) -> {
                    logger.info("--->setHandleChanges() START");
    
                    for (JsonNode document : docs) {
                        try {
                            //Change Feed hands the document to you in the form of a JsonNode
                            //As a developer you have two options for handling the JsonNode document provided to you by Change Feed
                            //One option is to operate on the document in the form of a JsonNode, as shown below. This is great
                            //especially if you do not have a single uniform data model for all documents.
                            logger.info("---->DOCUMENT RECEIVED: " + OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(document));
    
                            //You can also transform the JsonNode to a POJO having the same structure as the JsonNode,
                            //as shown below. Then you can operate on the POJO.
                            EnrichedPayment pojo_doc = OBJECT_MAPPER.treeToValue(document, EnrichedPayment.class);
                            logger.info("----=>id: " + pojo_doc.getId());

                            paymentConsumer.accept(pojo_doc);
    
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
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

            logger.info("-->START Change Feed Processor on worker (handles changes asynchronously)");

            Consumer<EnrichedPayment> paymentConsumer = payment -> {
                emitter.next(payment);
            };

            var changeFeedProcessorInstance = getChangeFeedProcessor("SampleHost_1", feedContainer, leaseContainer, paymentConsumer);
            changeFeedProcessorInstance.start()
            .subscribeOn(Schedulers.boundedElastic())
            .doOnSuccess(aVoid -> {
                //pass
                logger.info("subscribed");
            })
            .subscribe();
        });
    }
    
}
