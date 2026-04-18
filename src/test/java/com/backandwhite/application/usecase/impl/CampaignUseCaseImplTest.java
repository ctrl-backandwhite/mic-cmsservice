package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.application.port.out.CatalogPort;
import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.common.exception.BusinessException;
import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.Campaign;
import com.backandwhite.domain.repository.CampaignRepository;
import com.backandwhite.domain.valueobject.CampaignType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CampaignUseCaseImplTest {

    @Mock
    private CampaignRepository repository;

    @Mock
    private CatalogPort catalogPort;

    @InjectMocks
    private CampaignUseCaseImpl useCase;

    private Campaign pct(BigDecimal value) {
        return Campaign.builder().id("c1").name("camp").type(CampaignType.PERCENTAGE).value(Money.of(value))
                .startDate(Instant.parse("2025-01-01T00:00:00Z")).endDate(Instant.parse("2025-02-01T00:00:00Z"))
                .active(true).build();
    }

    @Test
    void create_valid_saves() {
        Campaign c = pct(new BigDecimal("10"));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of());
        when(repository.save(c)).thenReturn(c);

        assertThat(useCase.create(c)).isSameAs(c);
    }

    @Test
    void create_invalidDateRange_throws() {
        Campaign c = pct(new BigDecimal("10")).withStartDate(Instant.parse("2025-05-01T00:00:00Z"))
                .withEndDate(Instant.parse("2025-01-01T00:00:00Z"));
        assertThatThrownBy(() -> useCase.create(c)).isInstanceOf(BusinessException.class);
    }

    @Test
    void create_percentageZero_throws() {
        Campaign c = pct(BigDecimal.ZERO);
        assertThatThrownBy(() -> useCase.create(c)).isInstanceOf(BusinessException.class);
    }

    @Test
    void create_percentageTooHigh_throws() {
        Campaign c = pct(new BigDecimal("150"));
        assertThatThrownBy(() -> useCase.create(c)).isInstanceOf(BusinessException.class);
    }

    @Test
    void create_flashValid() {
        Campaign c = pct(new BigDecimal("50")).withType(CampaignType.FLASH);
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of());
        when(repository.save(c)).thenReturn(c);
        assertThat(useCase.create(c)).isSameAs(c);
    }

    @Test
    void create_fixedZero_throws() {
        Campaign c = pct(BigDecimal.ZERO).withType(CampaignType.FIXED);
        assertThatThrownBy(() -> useCase.create(c)).isInstanceOf(BusinessException.class);
    }

    @Test
    void create_fixedValid() {
        Campaign c = pct(new BigDecimal("20")).withType(CampaignType.FIXED);
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of());
        when(repository.save(c)).thenReturn(c);
        assertThat(useCase.create(c)).isSameAs(c);
    }

    @Test
    void create_bundle_missingBuyQty_throws() {
        Campaign c = pct(new BigDecimal("10")).withType(CampaignType.BUNDLE);
        assertThatThrownBy(() -> useCase.create(c)).isInstanceOf(BusinessException.class);
    }

    @Test
    void create_bundle_missingGetQty_throws() {
        Campaign c = pct(new BigDecimal("10")).withType(CampaignType.BUNDLE).withBuyQty(1);
        assertThatThrownBy(() -> useCase.create(c)).isInstanceOf(BusinessException.class);
    }

    @Test
    void create_bundle_valid() {
        Campaign c = pct(new BigDecimal("10")).withType(CampaignType.BUNDLE).withBuyQty(2).withGetQty(1);
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of());
        when(repository.save(c)).thenReturn(c);
        assertThat(useCase.create(c)).isSameAs(c);
    }

    @Test
    void create_buy2get1_valid() {
        Campaign c = pct(null).withType(CampaignType.BUY2GET1);
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of());
        when(repository.save(c)).thenReturn(c);
        assertThat(useCase.create(c)).isSameAs(c);
    }

    @Test
    void create_freeShipping_valid() {
        Campaign c = pct(null).withType(CampaignType.FREE_SHIPPING);
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of());
        when(repository.save(c)).thenReturn(c);
        assertThat(useCase.create(c)).isSameAs(c);
    }

    @Test
    void create_globalConflict_throws() {
        Campaign existing = pct(new BigDecimal("5")).withName("Existing");
        Campaign newCamp = pct(new BigDecimal("10"));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of(existing));
        assertThatThrownBy(() -> useCase.create(newCamp)).isInstanceOfSatisfying(BusinessException.class,
                e -> assertThat(e.getCode()).isEqualTo("OV001"));
    }

    @Test
    void create_differentScope_FSvsPCT_noConflict() {
        Campaign existing = pct(null).withType(CampaignType.FREE_SHIPPING).withName("FS");
        Campaign newCamp = pct(new BigDecimal("10"));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of(existing));
        when(repository.save(newCamp)).thenReturn(newCamp);
        assertThat(useCase.create(newCamp)).isSameAs(newCamp);
    }

    @Test
    void create_categoriesOverlap_throws() {
        Campaign existing = pct(new BigDecimal("5")).withName("E").withAppliesToCategories(List.of("catA"));
        Campaign newCamp = pct(new BigDecimal("10")).withAppliesToCategories(List.of("catA"));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of(existing));
        when(catalogPort.expandWithSubcategories(anyList())).thenReturn(Set.of("catA"));
        assertThatThrownBy(() -> useCase.create(newCamp)).isInstanceOfSatisfying(BusinessException.class,
                e -> assertThat(e.getCode()).isEqualTo("OV002"));
    }

    @Test
    void create_categoriesNoOverlap() {
        Campaign existing = pct(new BigDecimal("5")).withName("E").withAppliesToCategories(List.of("catA"));
        Campaign newCamp = pct(new BigDecimal("10")).withAppliesToCategories(List.of("catB"));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of(existing));
        when(catalogPort.expandWithSubcategories(List.of("catA"))).thenReturn(Set.of("catA"));
        when(catalogPort.expandWithSubcategories(List.of("catB"))).thenReturn(Set.of("catB"));
        when(repository.save(any())).thenReturn(newCamp);
        assertThat(useCase.create(newCamp)).isSameAs(newCamp);
    }

    @Test
    void create_productsOverlap_throws() {
        Campaign existing = pct(new BigDecimal("5")).withName("E").withAppliesToProducts(List.of("p1"));
        Campaign newCamp = pct(new BigDecimal("10")).withAppliesToProducts(List.of("p1"));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of(existing));
        assertThatThrownBy(() -> useCase.create(newCamp)).isInstanceOfSatisfying(BusinessException.class,
                e -> assertThat(e.getCode()).isEqualTo("OV003"));
    }

    @Test
    void create_productsNoOverlap() {
        Campaign existing = pct(new BigDecimal("5")).withName("E").withAppliesToProducts(List.of("p1"));
        Campaign newCamp = pct(new BigDecimal("10")).withAppliesToProducts(List.of("p2"));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of(existing));
        when(repository.save(any())).thenReturn(newCamp);
        assertThat(useCase.create(newCamp)).isSameAs(newCamp);
    }

    @Test
    void create_categoriesVsProducts_crossConflict() {
        Campaign existing = pct(new BigDecimal("5")).withName("E").withAppliesToProducts(List.of("p1"));
        Campaign newCamp = pct(new BigDecimal("10")).withAppliesToCategories(List.of("catA"));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of(existing));
        when(catalogPort.expandWithSubcategories(anyList())).thenReturn(Set.of("catA"));
        when(catalogPort.getProductCategoryMap(anyList())).thenReturn(Map.of("p1", "catA"));
        assertThatThrownBy(() -> useCase.create(newCamp)).isInstanceOfSatisfying(BusinessException.class,
                e -> assertThat(e.getCode()).isEqualTo("OV004"));
    }

    @Test
    void create_productsVsCategories_crossConflict() {
        Campaign existing = pct(new BigDecimal("5")).withName("E").withAppliesToCategories(List.of("catA"));
        Campaign newCamp = pct(new BigDecimal("10")).withAppliesToProducts(List.of("p1"));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of(existing));
        when(catalogPort.expandWithSubcategories(anyList())).thenReturn(Set.of("catA"));
        when(catalogPort.getProductCategoryMap(anyList())).thenReturn(Map.of("p1", "catA"));
        assertThatThrownBy(() -> useCase.create(newCamp)).isInstanceOfSatisfying(BusinessException.class,
                e -> assertThat(e.getCode()).isEqualTo("OV004"));
    }

    @Test
    void update_existing_saves() {
        Campaign existing = pct(new BigDecimal("10"));
        Campaign update = pct(new BigDecimal("20")).withId(null);
        when(repository.findById("c1")).thenReturn(Optional.of(existing));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of());
        when(repository.update(any())).thenAnswer(i -> i.getArgument(0));
        Campaign result = useCase.update("c1", update);
        assertThat(result.getId()).isEqualTo("c1");
    }

    @Test
    void update_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.update("x", pct(new BigDecimal("10"))))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findById_existing() {
        when(repository.findById("c1")).thenReturn(Optional.of(pct(new BigDecimal("10"))));
        assertThat(useCase.findById("c1")).isNotNull();
    }

    @Test
    void findById_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAll_asc() {
        Page<Campaign> p = new PageImpl<>(List.of(pct(new BigDecimal("10"))));
        when(repository.findAll(any(Map.class), any(Pageable.class))).thenReturn(p);
        assertThat(useCase.findAll(Map.of(), 0, 10, "createdAt", true).content()).hasSize(1);
    }

    @Test
    void findAll_desc() {
        Page<Campaign> p = new PageImpl<>(List.of(pct(new BigDecimal("10"))));
        when(repository.findAll(any(Map.class), any(Pageable.class))).thenReturn(p);
        assertThat(useCase.findAll(Map.of(), 0, 10, "createdAt", false).content()).hasSize(1);
    }

    @Test
    void findAllActive_delegates() {
        when(repository.findAllActive()).thenReturn(List.of(pct(new BigDecimal("10"))));
        assertThat(useCase.findAllActive()).hasSize(1);
    }

    @Test
    void delete_existing() {
        when(repository.findById("c1")).thenReturn(Optional.of(pct(new BigDecimal("10"))));
        useCase.delete("c1");
        verify(repository).delete("c1");
    }

    @Test
    void delete_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.delete("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void toggleActive_fromActiveToInactive_noOverlapCheck() {
        Campaign c = pct(new BigDecimal("10")).withActive(true);
        when(repository.findById("c1")).thenReturn(Optional.of(c));
        when(repository.update(any())).thenAnswer(i -> i.getArgument(0));

        useCase.toggleActive("c1");
        assertThat(c.isActive()).isFalse();
    }

    @Test
    void toggleActive_fromInactiveToActive_checksOverlap() {
        Campaign c = pct(new BigDecimal("10")).withActive(false);
        when(repository.findById("c1")).thenReturn(Optional.of(c));
        when(repository.findConflicting(any(), any(), any())).thenReturn(List.of());
        when(repository.update(any())).thenAnswer(i -> i.getArgument(0));

        useCase.toggleActive("c1");
        assertThat(c.isActive()).isTrue();
        verify(repository).findConflicting(any(), any(), any());
    }

    @Test
    void toggleActive_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.toggleActive("x")).isInstanceOf(EntityNotFoundException.class);
    }
}
