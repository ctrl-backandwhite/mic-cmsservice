package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.in.SeoPageDtoIn;
import com.backandwhite.api.dto.out.SeoPageDtoOut;
import com.backandwhite.domain.model.SeoPage;
import java.util.List;
import org.junit.jupiter.api.Test;

class SeoPageApiMapperTest {
    private final SeoPageApiMapper mapper = new SeoPageApiMapperImpl();

    @Test
    void nulls() {
        assertThat(mapper.toDto(null)).isNull();
        assertThat(mapper.toDtoList(null)).isNull();
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void toDto_copies() {
        SeoPage p = SeoPage.builder().id("p1").path("/x").metaTitle("T").indexable(true).build();
        SeoPageDtoOut out = mapper.toDto(p);
        assertThat(out.getId()).isEqualTo("p1");
        assertThat(out.isIndexable()).isTrue();
    }

    @Test
    void toDtoList() {
        assertThat(mapper.toDtoList(List.of(SeoPage.builder().id("p1").build()))).hasSize(1);
    }

    @Test
    void toDomain_ignoresId() {
        SeoPageDtoIn in = SeoPageDtoIn.builder().path("/x").metaTitle("T").build();
        SeoPage p = mapper.toDomain(in);
        assertThat(p.getId()).isNull();
    }
}
