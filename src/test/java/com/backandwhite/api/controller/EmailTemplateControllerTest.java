package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.EmailTemplateDtoIn;
import com.backandwhite.api.dto.out.EmailTemplateDtoOut;
import com.backandwhite.api.mapper.EmailTemplateApiMapper;
import com.backandwhite.application.usecase.EmailTemplateUseCase;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.domain.model.EmailTemplate;
import com.backandwhite.domain.valueobject.EmailTrigger;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailTemplateControllerTest {
    @Mock
    EmailTemplateUseCase useCase;
    @Mock
    EmailTemplateApiMapper mapper;
    @InjectMocks
    EmailTemplateController controller;

    private EmailTemplateDtoIn in() {
        return EmailTemplateDtoIn.builder().name("n").triggerType(EmailTrigger.WELCOME).subject("s").bodyHtml("b")
                .build();
    }

    @Test
    void findAll_allFilters() {
        PageResult<EmailTemplate> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.findAll(any(), anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(controller.findAll("cat", "WELCOME", "q", 0, 20, "createdAt", false, "tok").getStatusCode().value())
                .isEqualTo(200);
    }

    @Test
    void findAll_noFilters() {
        PageResult<EmailTemplate> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.findAll(any(), anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(controller.findAll(null, null, null, 0, 20, "createdAt", false, "tok").getStatusCode().value())
                .isEqualTo(200);
    }

    @Test
    void findById_ok() {
        EmailTemplate t = EmailTemplate.builder().id("t1").build();
        when(useCase.findById("t1")).thenReturn(t);
        when(mapper.toDto(t)).thenReturn(EmailTemplateDtoOut.builder().id("t1").build());
        assertThat(controller.findById("t1", "tok").getBody().getId()).isEqualTo("t1");
    }

    @Test
    void findByName_ok() {
        EmailTemplate t = EmailTemplate.builder().name("n").build();
        when(useCase.findByName("n")).thenReturn(t);
        when(mapper.toDto(t)).thenReturn(EmailTemplateDtoOut.builder().name("n").build());
        assertThat(controller.findByName("n", "tok").getBody().getName()).isEqualTo("n");
    }

    @Test
    void create_ok() {
        EmailTemplate t = EmailTemplate.builder().build();
        when(mapper.toDomain(any())).thenReturn(t);
        when(useCase.create(t)).thenReturn(t);
        when(mapper.toDto(t)).thenReturn(EmailTemplateDtoOut.builder().build());
        assertThat(controller.create(in(), "tok").getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void update_ok() {
        EmailTemplate t = EmailTemplate.builder().build();
        when(mapper.toDomain(any())).thenReturn(t);
        when(useCase.update(eq("t1"), any())).thenReturn(t);
        when(mapper.toDto(t)).thenReturn(EmailTemplateDtoOut.builder().build());
        assertThat(controller.update("t1", in(), "tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void delete_ok() {
        assertThat(controller.delete("t1", "tok").getStatusCode().value()).isEqualTo(204);
        verify(useCase).delete("t1");
    }
}
