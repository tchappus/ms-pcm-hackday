package com.myapp.lib.repo;

import org.springframework.stereotype.Repository;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.myapp.lib.EnrichedPayment;

@Repository
public interface PaymentsRepo extends ReactiveCosmosRepository<EnrichedPayment, String> {
}
