package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.NewsletterSubscribeDtoIn;
import com.backandwhite.api.dto.out.NewsletterSubscriberDtoOut;
import com.backandwhite.api.mapper.NewsletterApiMapper;
import com.backandwhite.application.usecase.NewsletterUseCase;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.domain.model.NewsletterSubscriber;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NewsletterControllerTest {
    @Mock
    NewsletterUseCase useCase;
    @Mock
    NewsletterApiMapper mapper;
    @InjectMocks
    NewsletterController controller;

    @Test
    void subscribe_ok() {
        NewsletterSubscribeDtoIn in = NewsletterSubscribeDtoIn.builder().email("a@a.com").source("src").build();
        NewsletterSubscriber s = NewsletterSubscriber.builder().build();
        when(useCase.subscribe("a@a.com", "src")).thenReturn(s);
        when(mapper.toDto(s)).thenReturn(NewsletterSubscriberDtoOut.builder().build());
        assertThat(controller.subscribe(in, "tok").getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void unsubscribe_ok() {
        assertThat(controller.unsubscribe("a@a.com", "tok").getStatusCode().value()).isEqualTo(204);
        verify(useCase).unsubscribe("a@a.com");
    }

    @Test
    void findAll_withFilters() {
        PageResult<NewsletterSubscriber> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.findAll(any(), anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(controller.findAll("ACTIVE", "q", 0, 20, "createdAt", false, "tok").getStatusCode().value())
                .isEqualTo(200);
    }

    @Test
    void findAll_noFilters() {
        PageResult<NewsletterSubscriber> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.findAll(any(), anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(controller.findAll(null, null, 0, 20, "createdAt", false, "tok").getStatusCode().value())
                .isEqualTo(200);
    }

    @Test
    void findById_ok() {
        NewsletterSubscriber s = NewsletterSubscriber.builder().id("s1").build();
        when(useCase.findById("s1")).thenReturn(s);
        when(mapper.toDto(s)).thenReturn(NewsletterSubscriberDtoOut.builder().id("s1").build());
        assertThat(controller.findById("s1", "tok").getBody().getId()).isEqualTo("s1");
    }

    @Test
    void delete_ok() {
        assertThat(controller.delete("s1", "tok").getStatusCode().value()).isEqualTo(204);
        verify(useCase).delete("s1");
    }

    @Test
    void count_ok() {
        when(useCase.count()).thenReturn(7L);
        assertThat(controller.count("tok").getBody()).isEqualTo(7L);
    }
}
