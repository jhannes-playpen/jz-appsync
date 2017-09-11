package com.soprasteria.javazone.product;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Product {

    @Getter @Setter
    private UUID id;

    @Getter @Setter
    private String productName, productCategory;

    @Getter @Setter
    private Integer priceInCents;

}
