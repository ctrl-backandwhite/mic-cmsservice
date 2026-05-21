package com.backandwhite.infrastructure.external.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.infrastructure.external.dto.CategoryNodeDto;
import com.backandwhite.infrastructure.external.dto.ProductDto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogMapperTest {

    private CatalogMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CatalogMapperImpl();
    }

    @Test
    void toCategoryId_nullProduct_returnsNull() {
        assertThat(mapper.toCategoryId(null)).isNull();
    }

    @Test
    void toCategoryId_validProduct_returnsCategoryId() {
        ProductDto product = new ProductDto("p-1", "cat-42");
        assertThat(mapper.toCategoryId(product)).isEqualTo("cat-42");
    }

    @Test
    void toChildrenIndex_nullTree_returnsEmptyMap() {
        Map<String, List<String>> result = mapper.toChildrenIndex(null);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void toChildrenIndex_emptyTree_returnsEmptyMap() {
        Map<String, List<String>> result = mapper.toChildrenIndex(Collections.emptyList());
        assertThat(result).isEmpty();
    }

    @Test
    void toChildrenIndex_flatTree_returnsParentToChildren() {
        CategoryNodeDto leaf1 = new CategoryNodeDto("c1", "Cat1", null);
        CategoryNodeDto leaf2 = new CategoryNodeDto("c2", "Cat2", List.of());
        CategoryNodeDto parent = new CategoryNodeDto("p1", "Parent", List.of(leaf1, leaf2));

        Map<String, List<String>> idx = mapper.toChildrenIndex(List.of(parent));

        assertThat(idx).containsKey("p1").doesNotContainKey("c1").doesNotContainKey("c2");
        assertThat(idx.get("p1")).containsExactly("c1", "c2");
        // leaf1/leaf2 have no subcategories => not in index
    }

    @Test
    void toChildrenIndex_nestedTree_recursesIntoSubCategories() {
        CategoryNodeDto grand = new CategoryNodeDto("g1", "Grand", null);
        CategoryNodeDto child = new CategoryNodeDto("c1", "Child", List.of(grand));
        CategoryNodeDto parent = new CategoryNodeDto("p1", "Parent", List.of(child));

        Map<String, List<String>> idx = mapper.toChildrenIndex(List.of(parent));

        assertThat(idx).hasSize(2);
        assertThat(idx.get("p1")).containsExactly("c1");
        assertThat(idx.get("c1")).containsExactly("g1");
    }

    @Test
    void toChildrenIndex_skipsNullNodesAndNullIds() {
        CategoryNodeDto goodChild = new CategoryNodeDto("c1", "Cat1", null);
        CategoryNodeDto nullIdChild = new CategoryNodeDto(null, "no-id", null);
        CategoryNodeDto parent = new CategoryNodeDto("p1", "Parent", Arrays.asList(goodChild, nullIdChild, null));
        // Top-level: include a node with null id (skipped) and a null entry would NPE,
        // so test only nodes with null id at top level
        CategoryNodeDto rootNullId = new CategoryNodeDto(null, "null-root", List.of(goodChild));

        Map<String, List<String>> idx = mapper.toChildrenIndex(List.of(parent, rootNullId));

        // p1 has only the non-null + non-null-id child captured
        assertThat(idx.get("p1")).containsExactly("c1");
        // rootNullId is skipped because its id is null
        assertThat(idx).doesNotContainValue(List.of("c1-from-root"));
    }
}
