package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.in.ContactMessageDtoIn;
import com.backandwhite.api.dto.out.ContactMessageDtoOut;
import com.backandwhite.domain.model.ContactMessage;
import java.util.List;
import org.junit.jupiter.api.Test;

class ContactApiMapperTest {
    private final ContactApiMapper mapper = new ContactApiMapperImpl();

    @Test
    void toDto_null_null() {
        assertThat(mapper.toDto(null)).isNull();
        assertThat(mapper.toDtoList(null)).isNull();
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void toDto_full() {
        ContactMessage m = ContactMessage.builder().id("m1").name("n").email("e@e.com").subject("s").message("msg")
                .read(true).build();
        ContactMessageDtoOut dto = mapper.toDto(m);
        assertThat(dto.getId()).isEqualTo("m1");
        assertThat(dto.isRead()).isTrue();
    }

    @Test
    void toDtoList_mapsAll() {
        ContactMessage m = ContactMessage.builder().id("m1").build();
        List<ContactMessageDtoOut> out = mapper.toDtoList(List.of(m));
        assertThat(out).hasSize(1);
    }

    @Test
    void toDomain_ignoresIdAndRead() {
        ContactMessageDtoIn in = ContactMessageDtoIn.builder().name("n").email("e@e.com").subject("s").message("msg")
                .build();
        ContactMessage m = mapper.toDomain(in);
        assertThat(m.getId()).isNull();
        assertThat(m.isRead()).isFalse();
        assertThat(m.getName()).isEqualTo("n");
    }
}
