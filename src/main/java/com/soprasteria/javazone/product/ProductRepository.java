package com.soprasteria.javazone.product;

import java.util.UUID;

public interface ProductRepository {

    void save(Product product);

    Product retrieve(UUID id);

}
