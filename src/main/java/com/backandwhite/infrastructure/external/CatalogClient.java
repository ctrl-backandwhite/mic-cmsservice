package com.backandwhite.infrastructure.external;

import com.backandwhite.application.port.out.CatalogPort;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * HTTP client for service-to-service calls to mic-productcategory. Used
 * exclusively during campaign overlap validation.
 */
@Log4j2
@Component
public class CatalogClient implements CatalogPort {

    private final RestClient restClient;

    public CatalogClient(@Value("${services.catalog.url:http://localhost:6002}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    /**
     * {@inheritDoc} Calls GET /api/v1/products/{id} for each product in the list
     * (in series).
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> getProductCategoryMap(List<String> productIds) {
        Map<String, String> result = new HashMap<>();
        if (productIds == null || productIds.isEmpty())
            return result;

        for (String productId : productIds) {
            try {
                Map<String, Object> product = restClient.get().uri("/api/v1/products/{id}", productId)
                        .header("X-nx036-auth", "service-cms").retrieve()
                        .body(new ParameterizedTypeReference<Map<String, Object>>() {
                        });
                if (product != null && product.get("categoryId") != null) {
                    result.put(productId, product.get("categoryId").toString());
                }
            } catch (Exception e) {
                log.warn("Could not resolve category for product {}: {}", productId, e.getMessage());
            }
        }
        return result;
    }

    /**
     * {@inheritDoc} Calls GET /api/v1/categories to retrieve the full tree, then
     * expands descendants.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<String> expandWithSubcategories(List<String> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty())
            return new HashSet<>();

        try {
            List<Map<String, Object>> tree = restClient.get().uri("/api/v1/categories?locale=en")
                    .header("X-nx036-auth", "service-cms").retrieve()
                    .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            if (tree == null)
                return new HashSet<>(categoryIds);

            // Build a flat map: categoryId → list of direct child IDs
            Map<String, List<String>> childrenMap = new HashMap<>();
            buildChildrenMap(tree, childrenMap);

            // Expand each input category to include all its descendants
            Set<String> expanded = new HashSet<>();
            for (String catId : categoryIds) {
                expanded.add(catId);
                collectDescendants(catId, childrenMap, expanded);
            }
            return expanded;

        } catch (Exception e) {
            log.warn("Could not expand subcategories (catalog unavailable): {}", e.getMessage());
            return new HashSet<>(categoryIds); // Fail-open: return only the original IDs
        }
    }

    /**
     * Recursively builds a map of parentId → [childId1, childId2, ...] from the
     * category tree.
     */
    @SuppressWarnings("unchecked")
    private void buildChildrenMap(List<Map<String, Object>> nodes, Map<String, List<String>> childrenMap) {
        for (Map<String, Object> node : nodes) {
            String parentId = node.get("id") != null ? node.get("id").toString() : null;
            List<Map<String, Object>> subs = (List<Map<String, Object>>) node.get("subCategories");
            if (parentId == null || subs == null || subs.isEmpty())
                continue;

            List<String> childIds = new ArrayList<>();
            for (Map<String, Object> sub : subs) {
                if (sub.get("id") != null)
                    childIds.add(sub.get("id").toString());
            }
            childrenMap.put(parentId, childIds);
            buildChildrenMap(subs, childrenMap); // Recurse into subcategories
        }
    }

    /** Adds all descendant IDs of {@code catId} into {@code result}. */
    private void collectDescendants(String catId, Map<String, List<String>> childrenMap, Set<String> result) {
        List<String> children = childrenMap.get(catId);
        if (children == null)
            return;
        for (String child : children) {
            if (result.add(child)) { // Avoid infinite cycles
                collectDescendants(child, childrenMap, result);
            }
        }
    }
}
