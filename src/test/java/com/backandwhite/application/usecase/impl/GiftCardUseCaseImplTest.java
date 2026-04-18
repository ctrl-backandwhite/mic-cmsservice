package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.application.port.out.CmsEventPort;
import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.common.exception.BusinessException;
import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import com.backandwhite.domain.repository.GiftCardRepository;
import com.backandwhite.domain.valueobject.GiftCardStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class GiftCardUseCaseImplTest {

    @Mock
    private GiftCardRepository repository;

    @Mock
    private CmsEventPort eventPort;

    @InjectMocks
    private GiftCardUseCaseImpl useCase;

    private GiftCardDesign design() {
        return GiftCardDesign.builder().id("d1").name("Xmas").active(true).build();
    }

    private GiftCard card() {
        return GiftCard.builder().id("gc1").code("GC-ABCD-EFGH-IJKL").originalAmount(Money.of(new BigDecimal("100.00")))
                .balance(Money.of(new BigDecimal("100.00"))).status(GiftCardStatus.ACTIVE).buyerId("u1")
                .recipientName("n").recipientEmail("r@r.com").build();
    }

    // --- Designs ---

    @Test
    void createDesign_delegates() {
        GiftCardDesign d = design();
        when(repository.saveDesign(d)).thenReturn(d);
        assertThat(useCase.createDesign(d)).isSameAs(d);
    }

    @Test
    void updateDesign_existing() {
        when(repository.findDesignById("d1")).thenReturn(Optional.of(design()));
        when(repository.updateDesign(any())).thenAnswer(i -> i.getArgument(0));
        GiftCardDesign upd = design().withId(null).withName("NewYear");
        GiftCardDesign result = useCase.updateDesign("d1", upd);
        assertThat(result.getId()).isEqualTo("d1");
    }

    @Test
    void updateDesign_notFound() {
        when(repository.findDesignById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.updateDesign("x", design())).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findDesignById_existing() {
        when(repository.findDesignById("d1")).thenReturn(Optional.of(design()));
        assertThat(useCase.findDesignById("d1")).isNotNull();
    }

    @Test
    void findDesignById_notFound() {
        when(repository.findDesignById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findDesignById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAllDesigns_delegates() {
        when(repository.findAllDesigns()).thenReturn(List.of(design()));
        assertThat(useCase.findAllDesigns()).hasSize(1);
    }

    @Test
    void findAllActiveDesigns_delegates() {
        when(repository.findAllActiveDesigns()).thenReturn(List.of(design()));
        assertThat(useCase.findAllActiveDesigns()).hasSize(1);
    }

    @Test
    void deleteDesign_existing() {
        when(repository.findDesignById("d1")).thenReturn(Optional.of(design()));
        useCase.deleteDesign("d1");
        verify(repository).deleteDesign("d1");
    }

    @Test
    void deleteDesign_notFound() {
        when(repository.findDesignById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.deleteDesign("x")).isInstanceOf(EntityNotFoundException.class);
    }

    // --- GiftCards ---

    @Test
    void purchase_createsWithGeneratedCodeAndExpiry() {
        GiftCard in = GiftCard.builder().originalAmount(Money.of(new BigDecimal("50.00"))).buyerId("u1")
                .recipientEmail("r@r.com").recipientName("n").build();
        when(repository.findByCode(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(GiftCard.class))).thenAnswer(i -> {
            GiftCard g = i.getArgument(0);
            g.setId("gen-id");
            return g;
        });

        GiftCard saved = useCase.purchase(in);
        assertThat(saved.getCode()).startsWith("GC-");
        assertThat(saved.getBalance()).isEqualTo(saved.getOriginalAmount());
        assertThat(saved.getStatus()).isEqualTo(GiftCardStatus.PENDING);
        assertThat(saved.getExpiryDate()).isNotNull();
        verify(repository).saveTransaction(any(GiftCardTransaction.class));
        verify(eventPort).publishGiftCardPurchased(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                any());
    }

    @Test
    void purchase_withPreexistingExpiry_preserves() {
        LocalDate expiry = LocalDate.of(2030, 1, 1);
        GiftCard in = GiftCard.builder().originalAmount(Money.of(new BigDecimal("50.00"))).expiryDate(expiry).build();
        when(repository.findByCode(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(GiftCard.class))).thenAnswer(i -> i.getArgument(0));

        GiftCard saved = useCase.purchase(in);
        assertThat(saved.getExpiryDate()).isEqualTo(expiry);
    }

    @Test
    void findById_existing() {
        when(repository.findById("gc1")).thenReturn(Optional.of(card()));
        assertThat(useCase.findById("gc1")).isNotNull();
    }

    @Test
    void findById_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findByCode_existing() {
        when(repository.findByCode("C1")).thenReturn(Optional.of(card()));
        assertThat(useCase.findByCode("C1")).isNotNull();
    }

    @Test
    void findByCode_notFound() {
        when(repository.findByCode("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findByCode("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAll_asc() {
        Page<GiftCard> p = new PageImpl<>(List.of(card()));
        when(repository.findAll(any(Map.class), any(Pageable.class))).thenReturn(p);
        assertThat(useCase.findAll(Map.of(), 0, 10, "createdAt", true).content()).hasSize(1);
    }

    @Test
    void findAll_desc() {
        Page<GiftCard> p = new PageImpl<>(List.of(card()));
        when(repository.findAll(any(Map.class), any(Pageable.class))).thenReturn(p);
        assertThat(useCase.findAll(Map.of(), 0, 10, "createdAt", false).content()).hasSize(1);
    }

    @Test
    void findByBuyerId_asc() {
        Page<GiftCard> p = new PageImpl<>(List.of(card()));
        when(repository.findByBuyerId(any(), any(Pageable.class))).thenReturn(p);
        assertThat(useCase.findByBuyerId("u1", 0, 10, "createdAt", true).content()).hasSize(1);
    }

    @Test
    void findByBuyerId_desc() {
        Page<GiftCard> p = new PageImpl<>(List.of(card()));
        when(repository.findByBuyerId(any(), any(Pageable.class))).thenReturn(p);
        assertThat(useCase.findByBuyerId("u1", 0, 10, "createdAt", false).content()).hasSize(1);
    }

    @Test
    void getBalance_existing_returnsBalance() {
        when(repository.findByCode("C")).thenReturn(Optional.of(card()));
        assertThat(useCase.getBalance("C").getAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void getBalance_notFound() {
        when(repository.findByCode("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.getBalance("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findMySent_returnsContent() {
        Page<GiftCard> p = new PageImpl<>(List.of(card()));
        when(repository.findByBuyerId(any(), any(Pageable.class))).thenReturn(p);
        assertThat(useCase.findMySent("u1")).hasSize(1);
    }

    @Test
    void findMyReceived_blankEmail_returnsEmpty() {
        assertThat(useCase.findMyReceived(null)).isEmpty();
        assertThat(useCase.findMyReceived("  ")).isEmpty();
    }

    @Test
    void findMyReceived_returnsList() {
        Page<GiftCard> p = new PageImpl<>(List.of(card()));
        when(repository.findByRecipientEmail(any(), any(Pageable.class))).thenReturn(p);
        assertThat(useCase.findMyReceived("r@r.com")).hasSize(1);
    }

    @Test
    void claimByCode_pending_activates() {
        GiftCard c = card().withStatus(GiftCardStatus.PENDING).withRecipientEmail(null).withActivatedAt(null);
        when(repository.findByCode("C")).thenReturn(Optional.of(c));
        when(repository.update(any(GiftCard.class))).thenAnswer(i -> i.getArgument(0));

        GiftCard result = useCase.claimByCode("C", "new@x.com");
        assertThat(result.getRecipientEmail()).isEqualTo("new@x.com");
        assertThat(result.getStatus()).isEqualTo(GiftCardStatus.ACTIVE);
        assertThat(result.getActivatedAt()).isNotNull();
    }

    @Test
    void claimByCode_alreadyActivatedWithEmail_throws() {
        GiftCard c = card().withStatus(GiftCardStatus.ACTIVE).withRecipientEmail("r@r.com")
                .withActivatedAt(java.time.Instant.now());
        when(repository.findByCode("C")).thenReturn(Optional.of(c));
        assertThatThrownBy(() -> useCase.claimByCode("C", "new@x.com")).isInstanceOf(BusinessException.class);
    }

    @Test
    void claimByCode_alreadyActiveNoChange_returnsSame() {
        GiftCard c = card().withStatus(GiftCardStatus.ACTIVE).withRecipientEmail("orig@x.com").withActivatedAt(null);
        when(repository.findByCode("C")).thenReturn(Optional.of(c));
        when(repository.update(any())).thenAnswer(i -> i.getArgument(0));
        GiftCard result = useCase.claimByCode("C", "new@x.com");
        assertThat(result.getActivatedAt()).isNotNull();
    }

    @Test
    void claimByCode_inactiveStatus_throws() {
        GiftCard c = card().withStatus(GiftCardStatus.EXPIRED);
        when(repository.findByCode("C")).thenReturn(Optional.of(c));
        assertThatThrownBy(() -> useCase.claimByCode("C", "new@x.com")).isInstanceOf(BusinessException.class);
    }

    @Test
    void claimByCode_notFound() {
        when(repository.findByCode("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.claimByCode("x", "e@e.com")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void redeem_activeSufficient_reducesBalance() {
        GiftCard c = card();
        when(repository.findByCode("C")).thenReturn(Optional.of(c));
        when(repository.saveTransaction(any(GiftCardTransaction.class))).thenAnswer(i -> i.getArgument(0));

        GiftCardTransaction tx = useCase.redeem("C", Money.of(new BigDecimal("40.00")), "o1");
        assertThat(c.getBalance().getAmount()).isEqualByComparingTo("60.00");
        assertThat(c.getStatus()).isEqualTo(GiftCardStatus.ACTIVE);
        assertThat(tx.getAmount().isNegative()).isTrue();
        verify(eventPort).publishGiftCardRedeemed(any(), any(), any(), any(), any(), any());
    }

    @Test
    void redeem_exhaustsBalance_marksUsed() {
        GiftCard c = card();
        when(repository.findByCode("C")).thenReturn(Optional.of(c));
        when(repository.saveTransaction(any())).thenAnswer(i -> i.getArgument(0));

        useCase.redeem("C", Money.of(new BigDecimal("100.00")), "o1");
        assertThat(c.getStatus()).isEqualTo(GiftCardStatus.USED);
    }

    @Test
    void redeem_notActive_throws() {
        GiftCard c = card().withStatus(GiftCardStatus.PENDING);
        when(repository.findByCode("C")).thenReturn(Optional.of(c));
        assertThatThrownBy(() -> useCase.redeem("C", Money.of(new BigDecimal("10.00")), "o"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void redeem_insufficient_throws() {
        GiftCard c = card();
        when(repository.findByCode("C")).thenReturn(Optional.of(c));
        assertThatThrownBy(() -> useCase.redeem("C", Money.of(new BigDecimal("500.00")), "o"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void redeem_notFound() {
        when(repository.findByCode("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.redeem("x", Money.of(BigDecimal.ONE), "o"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findTransactions_delegates() {
        when(repository.findTransactionsByGiftCardId("gc1")).thenReturn(List.of());
        assertThat(useCase.findTransactions("gc1")).isEmpty();
    }

    @Test
    void purchase_cannotGenerateUniqueCode_throwsIllegalState() {
        GiftCard in = GiftCard.builder().originalAmount(Money.of(new BigDecimal("10.00"))).build();
        when(repository.findByCode(anyString())).thenReturn(Optional.of(card())); // always duplicate
        assertThatThrownBy(() -> useCase.purchase(in)).isInstanceOf(IllegalStateException.class);
        verify(repository, never()).save(any());
    }
}
