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
public class CompetencyDto {

    private Long id;

    private String name;

    private String description;

    private String category;

    private Instant createdAt;
}
