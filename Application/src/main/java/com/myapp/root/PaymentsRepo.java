package com.myapp.root;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepo extends ReactiveCosmosRepository<EnrichedPayment, String> {
}
