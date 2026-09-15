package ru.indutranslator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRegulationDto {

    private Long id;

    private Long departmentId;

    private Long regulationId;

    private String regulationCode;

    private String regulationTitle;

    private Instant assignedAt;
}
