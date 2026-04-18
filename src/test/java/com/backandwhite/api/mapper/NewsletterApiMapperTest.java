package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.out.NewsletterSubscriberDtoOut;
import com.backandwhite.domain.model.NewsletterSubscriber;
import com.backandwhite.domain.valueobject.NewsletterStatus;
import java.util.List;
import org.junit.jupiter.api.Test;

class NewsletterApiMapperTest {
    private final NewsletterApiMapper mapper = new NewsletterApiMapperImpl();

    @Test
    void nulls() {
        assertThat(mapper.toDto(null)).isNull();
        assertThat(mapper.toDtoList(null)).isNull();
    }

    @Test
    void toDto_copies() {
        NewsletterSubscriber s = NewsletterSubscriber.builder().id("s1").email("a@a.com")
                .status(NewsletterStatus.ACTIVE).build();
        NewsletterSubscriberDtoOut out = mapper.toDto(s);
        assertThat(out.getId()).isEqualTo("s1");
        assertThat(out.getStatus()).isEqualTo(NewsletterStatus.ACTIVE);
    }

    @Test
    void toDtoList() {
        assertThat(mapper.toDtoList(List.of(NewsletterSubscriber.builder().id("s1").build()))).hasSize(1);
    }
}
