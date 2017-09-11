package com.soprasteria.javazone.product;

import java.util.UUID;

import org.jsonbuddy.JsonObject;

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

    public JsonObject toJson() {
        return new JsonObject()
            .put("id", id.toString())
            .put("product-name", productName)
            .put("product-category", productCategory)
            .put("price-in-cents", priceInCents);
    }

    public static Product fromJson(JsonObject json) {
        Product product = new Product();
        product.setId(UUID.fromString(json.requiredString("id")));
        product.setProductName(json.requiredString("product-name"));
        product.setProductCategory(json.stringValue("product-category").orElse(null));
        product.setPriceInCentsLong(json.longValue("price-in-cents").orElse(null));
        return product;
    }

    private void setPriceInCentsLong(Long price) {
        this.priceInCents = price != null ? (int)(long)price : null;
    }

}
