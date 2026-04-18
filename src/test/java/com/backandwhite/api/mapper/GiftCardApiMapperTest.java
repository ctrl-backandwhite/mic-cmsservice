package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.in.GiftCardDesignDtoIn;
import com.backandwhite.api.dto.in.GiftCardPurchaseDtoIn;
import com.backandwhite.api.dto.out.GiftCardDesignDtoOut;
import com.backandwhite.api.dto.out.GiftCardDtoOut;
import com.backandwhite.api.dto.out.GiftCardTransactionDtoOut;
import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import com.backandwhite.domain.valueobject.GiftCardStatus;
import com.backandwhite.domain.valueobject.GiftCardTransactionType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class GiftCardApiMapperTest {
    private final GiftCardApiMapper mapper = new GiftCardApiMapperImpl();

    @Test
    void nulls() {
        assertThat(mapper.toDesignDto(null)).isNull();
        assertThat(mapper.toDesignDtoList(null)).isNull();
        assertThat(mapper.toDesignDomain(null)).isNull();
        assertThat(mapper.toDto(null)).isNull();
        assertThat(mapper.toDtoList(null)).isNull();
        assertThat(mapper.toPurchaseDomain(null)).isNull();
        assertThat(mapper.toTransactionDto(null)).isNull();
        assertThat(mapper.toTransactionDtoList(null)).isNull();
        assertThat(mapper.moneyToBigDecimal(null)).isNull();
        assertThat(mapper.bigDecimalToMoney(null)).isNull();
    }

    @Test
    void toDesignDto_copies() {
        GiftCardDesign d = GiftCardDesign.builder().id("d1").name("n").active(true).build();
        GiftCardDesignDtoOut out = mapper.toDesignDto(d);
        assertThat(out.getId()).isEqualTo("d1");
    }

    @Test
    void toDesignDtoList() {
        assertThat(mapper.toDesignDtoList(List.of(GiftCardDesign.builder().id("d1").build()))).hasSize(1);
    }

    @Test
    void toDesignDomain_ignoresId() {
        GiftCardDesignDtoIn in = GiftCardDesignDtoIn.builder().name("n").active(true).build();
        GiftCardDesign d = mapper.toDesignDomain(in);
        assertThat(d.getId()).isNull();
    }

    @Test
    void toDto_copies() {
        GiftCard c = GiftCard.builder().id("gc1").code("C").status(GiftCardStatus.ACTIVE)
                .originalAmount(Money.of(new BigDecimal("50"))).balance(Money.of(new BigDecimal("50"))).build();
        GiftCardDtoOut out = mapper.toDto(c);
        assertThat(out.getId()).isEqualTo("gc1");
        assertThat(out.getOriginalAmount()).isEqualByComparingTo("50");
    }

    @Test
    void toDtoList() {
        assertThat(mapper.toDtoList(List.of(GiftCard.builder().id("gc1").build()))).hasSize(1);
    }

    @Test
    void toPurchaseDomain_mapsAmount() {
        GiftCardPurchaseDtoIn in = GiftCardPurchaseDtoIn.builder().designId("d1").amount(new BigDecimal("50"))
                .recipientName("n").recipientEmail("e@e.com").build();
        GiftCard c = mapper.toPurchaseDomain(in);
        assertThat(c.getOriginalAmount().getAmount()).isEqualByComparingTo("50");
        assertThat(c.getId()).isNull();
        assertThat(c.getCode()).isNull();
    }

    @Test
    void toTransactionDto_copies() {
        GiftCardTransaction tx = GiftCardTransaction.builder().id("tx1").giftCardId("gc1")
                .type(GiftCardTransactionType.PURCHASE).amount(Money.of(new BigDecimal("10"))).build();
        GiftCardTransactionDtoOut out = mapper.toTransactionDto(tx);
        assertThat(out.getId()).isEqualTo("tx1");
        assertThat(out.getAmount()).isEqualByComparingTo("10");
    }

    @Test
    void toTransactionDtoList() {
        assertThat(mapper.toTransactionDtoList(List.of(GiftCardTransaction.builder().id("tx1").build()))).hasSize(1);
    }

    @Test
    void moneyConversions() {
        assertThat(mapper.moneyToBigDecimal(Money.of(new BigDecimal("5")))).isEqualByComparingTo("5");
        assertThat(mapper.bigDecimalToMoney(new BigDecimal("5")).getAmount()).isEqualByComparingTo("5");
    }
}
