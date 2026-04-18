package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.in.EmailTemplateDtoIn;
import com.backandwhite.api.dto.out.EmailTemplateDtoOut;
import com.backandwhite.domain.model.EmailTemplate;
import com.backandwhite.domain.valueobject.EmailTrigger;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmailTemplateApiMapperTest {
    private final EmailTemplateApiMapper mapper = new EmailTemplateApiMapperImpl();

    @Test
    void nulls() {
        assertThat(mapper.toDto(null)).isNull();
        assertThat(mapper.toDtoList(null)).isNull();
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void toDto_copies() {
        EmailTemplate t = EmailTemplate.builder().id("t1").name("n").triggerType(EmailTrigger.WELCOME).subject("s")
                .bodyHtml("b").active(true).build();
        EmailTemplateDtoOut dto = mapper.toDto(t);
        assertThat(dto.getId()).isEqualTo("t1");
        assertThat(dto.getTriggerType()).isEqualTo(EmailTrigger.WELCOME);
    }

    @Test
    void toDtoList_mapsAll() {
        List<EmailTemplateDtoOut> list = mapper.toDtoList(List.of(EmailTemplate.builder().id("t1").build()));
        assertThat(list).hasSize(1);
    }

    @Test
    void toDomain_ignoresId() {
        EmailTemplateDtoIn in = EmailTemplateDtoIn.builder().name("n").triggerType(EmailTrigger.WELCOME).subject("s")
                .bodyHtml("b").build();
        EmailTemplate t = mapper.toDomain(in);
        assertThat(t.getId()).isNull();
        assertThat(t.getName()).isEqualTo("n");
    }
}
