package com.backandwhite.contract;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.LambdaDsl;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.backandwhite.infrastructure.external.CatalogClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "catalog-service", pactVersion = PactSpecVersion.V3)
class CatalogClientPactTest {

    static final String PRODUCT_ID = "prod-001";
    static final String CATEGORY_ID = "cat-electronics-001";

    @Pact(consumer = "CatalogConsumer", provider = "catalog-service")
    RequestResponsePact getProductById(PactDslWithProvider builder) {
        return builder
                .given("product prod-001 exists with category cat-electronics-001")
                .uponReceiving("a request to get product by id")
                .path("/api/v1/products/" + PRODUCT_ID)
                .method("GET")
                .headers("X-nx036-auth", "service-cms")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .stringType("id", PRODUCT_ID)
                        .stringType("categoryId", CATEGORY_ID))
                .toPact();
    }

    @Pact(consumer = "CatalogConsumer", provider = "catalog-service")
    RequestResponsePact getCategoryTree(PactDslWithProvider builder) {
        return builder
                .given("category tree exists with electronics category")
                .uponReceiving("a request to get category tree in English")
                .path("/api/v1/categories")
                .query("locale=en")
                .method("GET")
                .headers("X-nx036-auth", "service-cms")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(LambdaDsl.newJsonArray(array -> array.object(obj -> {
                    obj.stringType("id", CATEGORY_ID);
                    obj.array("subCategories", subs -> {
                    });
                })).build())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getProductById")
    void getProductCategoryMap_returnsCategoryForProduct(MockServer mockServer) {
        CatalogClient client = new CatalogClient(mockServer.getUrl());

        Map<String, String> result = client.getProductCategoryMap(List.of(PRODUCT_ID));

        assertThat(result).containsEntry(PRODUCT_ID, CATEGORY_ID);
    }

    @Test
    @PactTestFor(pactMethod = "getCategoryTree")
    void expandWithSubcategories_returnsCategoryInResult(MockServer mockServer) {
        CatalogClient client = new CatalogClient(mockServer.getUrl());

        Set<String> result = client.expandWithSubcategories(List.of(CATEGORY_ID));

        assertThat(result).contains(CATEGORY_ID);
    }
}
