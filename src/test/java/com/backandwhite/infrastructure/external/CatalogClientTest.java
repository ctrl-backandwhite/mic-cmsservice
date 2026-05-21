package com.backandwhite.infrastructure.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

class CatalogClientTest {

    private CatalogClient client;
    private RestClient restClient;
    @SuppressWarnings("rawtypes")
    private RestClient.RequestHeadersUriSpec uriSpec;
    @SuppressWarnings("rawtypes")
    private RestClient.RequestHeadersSpec headersSpec;
    private RestClient.ResponseSpec responseSpec;

    @BeforeEach
    @SuppressWarnings({"unchecked", "rawtypes"})
    void setUp() throws Exception {
        client = new CatalogClient("http://localhost:6002");
        restClient = mock(RestClient.class);
        uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        headersSpec = mock(RestClient.RequestHeadersSpec.class);
        responseSpec = mock(RestClient.ResponseSpec.class);

        Field f = CatalogClient.class.getDeclaredField("restClient");
        f.setAccessible(true);
        f.set(client, restClient);

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(uriSpec.uri(anyString(), any(Object[].class))).thenReturn(headersSpec);
        when(uriSpec.uri(anyString(), any(Object.class))).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), any(String[].class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void stubBody(Object body) {
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(body);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void stubBodyThrows(RuntimeException ex) {
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenThrow(ex);
    }

    @Test
    void getProductCategoryMap_nullList_returnsEmpty() {
        assertThat(client.getProductCategoryMap(null)).isEmpty();
    }

    @Test
    void getProductCategoryMap_emptyList_returnsEmpty() {
        assertThat(client.getProductCategoryMap(List.of())).isEmpty();
    }

    @Test
    void getProductCategoryMap_validProduct_extractsCategoryId() {
        Map<String, Object> body = new HashMap<>();
        body.put("id", "p1");
        body.put("categoryId", "cat-1");
        stubBody(body);

        Map<String, String> result = client.getProductCategoryMap(List.of("p1"));

        assertThat(result).containsEntry("p1", "cat-1");
    }

    @Test
    void getProductCategoryMap_nullBody_skipsProduct() {
        stubBody(null);

        Map<String, String> result = client.getProductCategoryMap(List.of("p1"));

        assertThat(result).isEmpty();
    }

    @Test
    void getProductCategoryMap_bodyWithoutCategoryId_skips() {
        Map<String, Object> body = new HashMap<>();
        body.put("id", "p1");
        stubBody(body);

        Map<String, String> result = client.getProductCategoryMap(List.of("p1"));

        assertThat(result).isEmpty();
    }

    @Test
    void getProductCategoryMap_runtimeException_loggedAndContinues() {
        stubBodyThrows(new ResourceAccessException("timeout"));

        Map<String, String> result = client.getProductCategoryMap(List.of("p1"));

        assertThat(result).isEmpty();
    }

    @Test
    void expandWithSubcategories_nullList_returnsEmpty() {
        assertThat(client.expandWithSubcategories(null)).isEmpty();
    }

    @Test
    void expandWithSubcategories_emptyList_returnsEmpty() {
        assertThat(client.expandWithSubcategories(List.of())).isEmpty();
    }

    @Test
    void expandWithSubcategories_nullTree_returnsOriginalIds() {
        stubBody(null);

        Set<String> result = client.expandWithSubcategories(List.of("c1", "c2"));

        assertThat(result).containsExactlyInAnyOrder("c1", "c2");
    }

    @Test
    void expandWithSubcategories_validTree_expandsDescendants() {
        Map<String, Object> grand = new HashMap<>();
        grand.put("id", "g1");
        grand.put("subCategories", List.of());

        Map<String, Object> child = new HashMap<>();
        child.put("id", "c1");
        child.put("subCategories", List.of(grand));

        Map<String, Object> root = new HashMap<>();
        root.put("id", "p1");
        root.put("subCategories", List.of(child));

        Map<String, Object> orphan = new HashMap<>();
        orphan.put("id", "x9");

        List<Map<String, Object>> tree = List.of(root, orphan);
        stubBody(tree);

        Set<String> result = client.expandWithSubcategories(List.of("p1", "x9"));

        assertThat(result).containsExactlyInAnyOrder("p1", "c1", "g1", "x9");
    }

    @Test
    void expandWithSubcategories_runtimeException_failsOpenWithOriginalIds() {
        stubBodyThrows(new ResourceAccessException("network"));

        Set<String> result = client.expandWithSubcategories(List.of("c1"));

        assertThat(result).containsExactly("c1");
    }

    @Test
    void expandWithSubcategories_skipsNodesWithoutIdOrSubs() {
        Map<String, Object> nullId = new HashMap<>();
        nullId.put("subCategories", List.of(Map.of("id", "x")));

        Map<String, Object> nullSub = new HashMap<>();
        nullSub.put("id", "noSubs");

        Map<String, Object> root = new HashMap<>();
        root.put("id", "p1");
        Map<String, Object> noIdChild = new HashMap<>();
        Map<String, Object> validChild = new HashMap<>();
        validChild.put("id", "c1");
        root.put("subCategories", List.of(noIdChild, validChild));

        List<Map<String, Object>> tree = List.of(nullId, nullSub, root);
        stubBody(tree);

        Set<String> result = client.expandWithSubcategories(List.of("p1"));

        assertThat(result).containsExactlyInAnyOrder("p1", "c1");
    }
}
