package com.backandwhite.application.port.out;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Port for consulting the product catalog service during campaign overlap
 * checks.
 * Used to resolve cross-scope conflicts: CATEGORIES ↔ PRODUCTS.
 */
public interface CatalogPort {

    /**
     * Returns a map of productId → categoryId for the given product IDs.
     * Products that cannot be resolved are omitted from the map.
     * Never throws — returns an empty map if the catalog is unavailable.
     */
    Map<String, String> getProductCategoryMap(List<String> productIds);

    /**
     * Given a list of category IDs, returns a set containing those IDs plus all
     * descendant (subcategory) IDs.
     * E.g.: "Computer & Office" → {"Computer & Office", "Laptops", "Storage
     * Devices", ...}
     * Never throws — returns the original IDs unchanged if the catalog is
     * unavailable.
     */
    Set<String> expandWithSubcategories(List<String> categoryIds);
}
