package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.ContactMessageDtoIn;
import com.backandwhite.api.dto.out.ContactMessageDtoOut;
import com.backandwhite.api.mapper.ContactApiMapper;
import com.backandwhite.application.usecase.ContactUseCase;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.domain.model.ContactMessage;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {
    @Mock
    ContactUseCase useCase;
    @Mock
    ContactApiMapper mapper;
    @InjectMocks
    ContactController controller;

    @Test
    void submit_ok() {
        ContactMessageDtoIn in = ContactMessageDtoIn.builder().name("n").email("e@e.com").subject("s").message("m")
                .build();
        ContactMessage m = ContactMessage.builder().build();
        when(mapper.toDomain(in)).thenReturn(m);
        when(useCase.submit(m)).thenReturn(m);
        when(mapper.toDto(m)).thenReturn(ContactMessageDtoOut.builder().build());
        assertThat(controller.submit(in, "tok").getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void findAll_ok() {
        PageResult<ContactMessage> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.findAll(anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(controller.findAll(0, 20, "createdAt", false, "tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findById_ok() {
        ContactMessage m = ContactMessage.builder().id("m1").build();
        when(useCase.findById("m1")).thenReturn(m);
        when(mapper.toDto(m)).thenReturn(ContactMessageDtoOut.builder().id("m1").build());
        assertThat(controller.findById("m1", "tok").getBody().getId()).isEqualTo("m1");
    }

    @Test
    void markAsRead_ok() {
        assertThat(controller.markAsRead("m1", "tok").getStatusCode().value()).isEqualTo(204);
        verify(useCase).markAsRead("m1");
    }

    @SuppressWarnings("unused")
    private ContactMessage dummy() {
        return ContactMessage.builder().build();
    }

    @SuppressWarnings("unused")
    private Object anyObj() {
        return any();
    }
}
