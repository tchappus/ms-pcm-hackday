package com.myapp.root;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BicCodeRepo extends CrudRepository<BicCode, String> {
}
