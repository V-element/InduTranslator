package ru.indutranslator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private Long id;

    private String name;

    private String description;

    private Long parentId;

    private String parentName;

    private Long enterpriseId;

    private String enterpriseName;

    private CommunicationProfileDto communicationProfile;

    private List<DepartmentRegulationDto> regulations;

    private List<CompetencyDto> competencies;

    private Instant createdAt;
}
