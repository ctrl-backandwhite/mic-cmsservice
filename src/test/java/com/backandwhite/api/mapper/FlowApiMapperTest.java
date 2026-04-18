package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.in.FlowDtoIn;
import com.backandwhite.api.dto.in.FlowStepDtoIn;
import com.backandwhite.api.dto.out.FlowDtoOut;
import com.backandwhite.api.dto.out.FlowStepDtoOut;
import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import com.backandwhite.domain.valueobject.FlowType;
import java.util.List;
import org.junit.jupiter.api.Test;

class FlowApiMapperTest {
    private final FlowApiMapper mapper = new FlowApiMapperImpl();

    @Test
    void nulls() {
        assertThat(mapper.toDto(null)).isNull();
        assertThat(mapper.toDtoList(null)).isNull();
        assertThat(mapper.toDomain(null)).isNull();
        assertThat(mapper.toStepDto(null)).isNull();
        assertThat(mapper.toStepDtoList(null)).isNull();
        assertThat(mapper.toStepDomain(null)).isNull();
        assertThat(mapper.toStepDomainList(null)).isNull();
    }

    @Test
    void toDto_flow() {
        Flow f = Flow.builder().id("f1").name("n").type(FlowType.DELIVERY).active(true).build();
        FlowDtoOut out = mapper.toDto(f);
        assertThat(out.getId()).isEqualTo("f1");
        assertThat(out.getType()).isEqualTo(FlowType.DELIVERY);
    }

    @Test
    void toDtoList_mapsAll() {
        assertThat(mapper.toDtoList(List.of(Flow.builder().id("f1").build()))).hasSize(1);
    }

    @Test
    void toDomain_ignoresId() {
        FlowDtoIn in = FlowDtoIn.builder().name("n").type(FlowType.DELIVERY).build();
        Flow f = mapper.toDomain(in);
        assertThat(f.getId()).isNull();
        assertThat(f.getSteps()).isNull();
    }

    @Test
    void toStepDto_copies() {
        FlowStep s = FlowStep.builder().id("s1").title("t").position(1).build();
        FlowStepDtoOut out = mapper.toStepDto(s);
        assertThat(out.getId()).isEqualTo("s1");
    }

    @Test
    void toStepDtoList_mapsAll() {
        assertThat(mapper.toStepDtoList(List.of(FlowStep.builder().id("s1").build()))).hasSize(1);
    }

    @Test
    void toStepDomain_ignoresId() {
        FlowStepDtoIn in = FlowStepDtoIn.builder().title("t").build();
        FlowStep s = mapper.toStepDomain(in);
        assertThat(s.getId()).isNull();
        assertThat(s.getFlowId()).isNull();
    }

    @Test
    void toStepDomainList_mapsAll() {
        assertThat(mapper.toStepDomainList(List.of(FlowStepDtoIn.builder().title("t").build()))).hasSize(1);
    }
}
