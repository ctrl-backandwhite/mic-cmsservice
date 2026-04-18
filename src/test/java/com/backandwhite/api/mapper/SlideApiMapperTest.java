package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.in.SlideDtoIn;
import com.backandwhite.api.dto.out.SlideDtoOut;
import com.backandwhite.domain.model.Slide;
import java.util.List;
import org.junit.jupiter.api.Test;

class SlideApiMapperTest {

    private final SlideApiMapper mapper = new SlideApiMapperImpl();

    @Test
    void toDto_copiesFields() {
        Slide s = Slide.builder().id("s1").title("t").subtitle("sub").imageUrl("u").link("l").buttonText("b")
                .position(1).active(true).build();
        SlideDtoOut dto = mapper.toDto(s);
        assertThat(dto).usingRecursiveComparison().ignoringFields("createdAt", "updatedAt")
                .isEqualTo(SlideDtoOut.builder().id("s1").title("t").subtitle("sub").imageUrl("u").link("l")
                        .buttonText("b").position(1).active(true).build());
    }

    @Test
    void toDto_null_returnsNull() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    void toDtoList_mapsAll() {
        Slide s = Slide.builder().id("s1").title("t").imageUrl("u").position(1).active(true).build();
        List<SlideDtoOut> list = mapper.toDtoList(List.of(s));
        assertThat(list).hasSize(1);
    }

    @Test
    void toDtoList_null_returnsNull() {
        assertThat(mapper.toDtoList(null)).isNull();
    }

    @Test
    void toDomain_mapsFields_ignoresIdAndTimestamps() {
        SlideDtoIn in = SlideDtoIn.builder().title("t").imageUrl("u").position(1).active(true).build();
        Slide s = mapper.toDomain(in);
        assertThat(s.getId()).isNull();
        assertThat(s.getCreatedAt()).isNull();
        assertThat(s.getTitle()).isEqualTo("t");
    }

    @Test
    void toDomain_null_returnsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }
}
