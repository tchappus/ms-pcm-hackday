package com.myapp.writer.repo;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.myapp.lib.EnrichedPayment;

import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepo extends ReactiveCosmosRepository<EnrichedPayment, String> {
}
