package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.GiftCardDesignDtoIn;
import com.backandwhite.api.dto.in.GiftCardPurchaseDtoIn;
import com.backandwhite.api.dto.in.GiftCardRedeemDtoIn;
import com.backandwhite.api.dto.out.GiftCardDesignDtoOut;
import com.backandwhite.api.dto.out.GiftCardDtoOut;
import com.backandwhite.api.dto.out.GiftCardTransactionDtoOut;
import com.backandwhite.api.mapper.GiftCardApiMapper;
import com.backandwhite.application.usecase.GiftCardUseCase;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GiftCardControllerTest {
    @Mock
    GiftCardUseCase useCase;
    @Mock
    GiftCardApiMapper mapper;
    @InjectMocks
    GiftCardController controller;

    @Test
    void findActiveDesigns_ok() {
        when(useCase.findAllActiveDesigns()).thenReturn(List.of());
        when(mapper.toDesignDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findActiveDesigns("tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findAllDesigns_ok() {
        when(useCase.findAllDesigns()).thenReturn(List.of());
        when(mapper.toDesignDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findAllDesigns("tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findDesignById_ok() {
        GiftCardDesign d = GiftCardDesign.builder().id("d1").build();
        when(useCase.findDesignById("d1")).thenReturn(d);
        when(mapper.toDesignDto(d)).thenReturn(GiftCardDesignDtoOut.builder().id("d1").build());
        assertThat(controller.findDesignById("tok", "d1").getBody().getId()).isEqualTo("d1");
    }

    @Test
    void createDesign_ok() {
        GiftCardDesignDtoIn in = GiftCardDesignDtoIn.builder().name("n").build();
        GiftCardDesign d = GiftCardDesign.builder().build();
        when(mapper.toDesignDomain(any())).thenReturn(d);
        when(useCase.createDesign(d)).thenReturn(d);
        when(mapper.toDesignDto(d)).thenReturn(GiftCardDesignDtoOut.builder().build());
        assertThat(controller.createDesign("tok", in).getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void updateDesign_ok() {
        GiftCardDesignDtoIn in = GiftCardDesignDtoIn.builder().name("n").build();
        GiftCardDesign d = GiftCardDesign.builder().build();
        when(mapper.toDesignDomain(any())).thenReturn(d);
        when(useCase.updateDesign(eq("d1"), any())).thenReturn(d);
        when(mapper.toDesignDto(d)).thenReturn(GiftCardDesignDtoOut.builder().build());
        assertThat(controller.updateDesign("tok", "d1", in).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void deleteDesign_ok() {
        assertThat(controller.deleteDesign("tok", "d1").getStatusCode().value()).isEqualTo(204);
        verify(useCase).deleteDesign("d1");
    }

    @Test
    void purchase_ok() {
        GiftCardPurchaseDtoIn in = GiftCardPurchaseDtoIn.builder().amount(new BigDecimal("10")).recipientName("r")
                .recipientEmail("r@r.com").build();
        GiftCard c = GiftCard.builder().build();
        when(mapper.toPurchaseDomain(any())).thenReturn(c);
        when(useCase.purchase(any())).thenReturn(c);
        when(mapper.toDto(c)).thenReturn(GiftCardDtoOut.builder().build());
        assertThat(controller.purchase("tok", "u1", in).getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void findByCode_ok() {
        GiftCard c = GiftCard.builder().code("C").build();
        when(useCase.findByCode("C")).thenReturn(c);
        when(mapper.toDto(c)).thenReturn(GiftCardDtoOut.builder().code("C").build());
        assertThat(controller.findByCode("tok", "C").getBody().getCode()).isEqualTo("C");
    }

    @Test
    void claimByCode_ok() {
        GiftCard c = GiftCard.builder().build();
        when(useCase.claimByCode("C", "e@e.com")).thenReturn(c);
        when(mapper.toDto(c)).thenReturn(GiftCardDtoOut.builder().build());
        assertThat(controller.claimByCode("tok", "e@e.com", "C").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void getBalance_ok() {
        when(useCase.getBalance("C")).thenReturn(Money.of(new BigDecimal("50")));
        assertThat(controller.getBalance("tok", "C").getBody()).isEqualByComparingTo("50");
    }

    @Test
    void redeem_ok() {
        GiftCardRedeemDtoIn in = GiftCardRedeemDtoIn.builder().code("C").amount(new BigDecimal("10")).orderId("o")
                .build();
        GiftCardTransaction tx = GiftCardTransaction.builder().build();
        when(useCase.redeem(eq("C"), any(), eq("o"))).thenReturn(tx);
        when(mapper.toTransactionDto(tx)).thenReturn(GiftCardTransactionDtoOut.builder().build());
        assertThat(controller.redeem("tok", in).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void getMySent_ok() {
        when(useCase.findMySent("u1")).thenReturn(List.of());
        assertThat(controller.getMySent("tok", "u1").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void getMyReceived_ok() {
        when(useCase.findMyReceived("e@e.com")).thenReturn(List.of());
        assertThat(controller.getMyReceived("tok", "e@e.com").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findAll_withFilters() {
        PageResult<GiftCard> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.findAll(any(), anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(controller.findAll("tok", "ACTIVE", "q", 0, 20, "createdAt", false).getStatusCode().value())
                .isEqualTo(200);
    }

    @Test
    void findAll_noFilters() {
        PageResult<GiftCard> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.findAll(any(), anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(controller.findAll("tok", null, null, 0, 20, "createdAt", false).getStatusCode().value())
                .isEqualTo(200);
    }

    @Test
    void findById_ok() {
        GiftCard c = GiftCard.builder().id("gc1").build();
        when(useCase.findById("gc1")).thenReturn(c);
        when(mapper.toDto(c)).thenReturn(GiftCardDtoOut.builder().id("gc1").build());
        assertThat(controller.findById("tok", "gc1").getBody().getId()).isEqualTo("gc1");
    }

    @Test
    void findTransactions_ok() {
        when(useCase.findTransactions("gc1")).thenReturn(List.of());
        when(mapper.toTransactionDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findTransactions("tok", "gc1").getStatusCode().value()).isEqualTo(200);
    }
}
