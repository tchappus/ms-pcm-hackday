package com.myapp.enricher.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.myapp.enricher.model.BicCode;

@Repository
public interface BicCodeRepo extends CrudRepository<BicCode, String> {
}
