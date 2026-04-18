package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.CampaignDtoIn;
import com.backandwhite.api.dto.out.CampaignDtoOut;
import com.backandwhite.api.mapper.CampaignApiMapper;
import com.backandwhite.application.usecase.CampaignUseCase;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.domain.model.Campaign;
import com.backandwhite.domain.valueobject.CampaignType;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CampaignControllerTest {
    @Mock
    CampaignUseCase useCase;
    @Mock
    CampaignApiMapper mapper;
    @InjectMocks
    CampaignController controller;

    private CampaignDtoIn in() {
        return CampaignDtoIn.builder().name("n").type(CampaignType.PERCENTAGE).startDate(Instant.EPOCH)
                .endDate(Instant.EPOCH).build();
    }

    @Test
    void findAllActive_ok() {
        when(useCase.findAllActive()).thenReturn(List.of());
        when(mapper.toDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findAllActive("tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findAll_withFilters() {
        PageResult<Campaign> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.findAll(any(), anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(
                controller.findAll("tok", true, "PERCENTAGE", "q", 0, 20, "createdAt", false).getStatusCode().value())
                .isEqualTo(200);
    }

    @Test
    void findAll_noFilters() {
        PageResult<Campaign> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.findAll(any(), anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(controller.findAll("tok", null, null, null, 0, 20, "createdAt", false).getStatusCode().value())
                .isEqualTo(200);
    }

    @Test
    void findById_ok() {
        Campaign c = Campaign.builder().id("c1").build();
        when(useCase.findById("c1")).thenReturn(c);
        when(mapper.toDto(c)).thenReturn(CampaignDtoOut.builder().id("c1").build());
        assertThat(controller.findById("tok", "c1").getBody().getId()).isEqualTo("c1");
    }

    @Test
    void create_ok() {
        Campaign c = Campaign.builder().build();
        when(mapper.toDomain(any())).thenReturn(c);
        when(useCase.create(c)).thenReturn(c);
        when(mapper.toDto(c)).thenReturn(CampaignDtoOut.builder().build());
        assertThat(controller.create("tok", in()).getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void update_ok() {
        Campaign c = Campaign.builder().build();
        when(mapper.toDomain(any())).thenReturn(c);
        when(useCase.update(eq("c1"), any())).thenReturn(c);
        when(mapper.toDto(c)).thenReturn(CampaignDtoOut.builder().build());
        assertThat(controller.update("tok", "c1", in()).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void delete_ok() {
        assertThat(controller.delete("tok", "c1").getStatusCode().value()).isEqualTo(204);
        verify(useCase).delete("c1");
    }

    @Test
    void toggleActive_ok() {
        assertThat(controller.toggleActive("tok", "c1").getStatusCode().value()).isEqualTo(204);
        verify(useCase).toggleActive("c1");
    }

    @SuppressWarnings("unused")
    private Map<String, Object> filters() {
        return Map.of();
    }
}
