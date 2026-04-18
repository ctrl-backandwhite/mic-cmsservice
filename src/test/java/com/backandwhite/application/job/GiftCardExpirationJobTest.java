package com.backandwhite.application.job;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.repository.GiftCardRepository;
import com.backandwhite.domain.valueobject.GiftCardStatus;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GiftCardExpirationJobTest {

    @Mock
    private GiftCardRepository repository;

    @InjectMocks
    private GiftCardExpirationJob job;

    @Test
    void expireGiftCards_noCards_doesNothing() {
        when(repository.findExpiredCards(ArgumentMatchers.any(LocalDate.class))).thenReturn(List.of());
        job.expireGiftCards();
        verify(repository, never()).update(ArgumentMatchers.any());
    }

    @Test
    void expireGiftCards_expiresAll() {
        GiftCard c1 = GiftCard.builder().id("1").status(GiftCardStatus.ACTIVE).build();
        GiftCard c2 = GiftCard.builder().id("2").status(GiftCardStatus.PENDING).build();
        when(repository.findExpiredCards(ArgumentMatchers.any(LocalDate.class))).thenReturn(List.of(c1, c2));

        job.expireGiftCards();

        verify(repository).update(c1);
        verify(repository).update(c2);
    }
}
