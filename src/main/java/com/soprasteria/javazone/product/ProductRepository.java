package com.soprasteria.javazone.product;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ProductRepository {

    void save(Product product);

    Product retrieve(UUID id);

    List<Product> listChanges(Instant since);

}
