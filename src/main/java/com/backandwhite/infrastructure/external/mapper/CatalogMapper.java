package com.backandwhite.infrastructure.external.mapper;

import com.backandwhite.infrastructure.external.dto.CategoryNodeDto;
import com.backandwhite.infrastructure.external.dto.ProductDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;

/**
 * Maps catalog-service HTTP DTOs into the shapes consumed internally by
 * {@link com.backandwhite.infrastructure.external.CatalogAdapter}: a flat
 * parent → children index, and simple {@code productId → categoryId} pairs.
 */
@Mapper(componentModel = "spring")
public interface CatalogMapper {

    /**
     * Flatten a category tree into a {@code parentId → [childId, ...]} map for
     * descendant expansion.
     */
    default Map<String, List<String>> toChildrenIndex(List<CategoryNodeDto> tree) {
        Map<String, List<String>> index = new HashMap<>();
        if (tree == null) {
            return index;
        }
        buildChildrenIndex(tree, index);
        return index;
    }

    /** Null-safe extractor for the {@code categoryId} of a {@link ProductDto}. */
    default String toCategoryId(ProductDto product) {
        return product == null ? null : product.categoryId();
    }

    private void buildChildrenIndex(List<CategoryNodeDto> nodes, Map<String, List<String>> index) {
        for (CategoryNodeDto node : nodes) {
            if (node == null || node.id() == null) {
                continue;
            }
            List<CategoryNodeDto> subs = node.subCategories();
            if (subs == null || subs.isEmpty()) {
                continue;
            }
            List<String> childIds = new ArrayList<>();
            for (CategoryNodeDto sub : subs) {
                if (sub != null && sub.id() != null) {
                    childIds.add(sub.id());
                }
            }
            index.put(node.id(), childIds);
            buildChildrenIndex(subs, index);
        }
    }
}
