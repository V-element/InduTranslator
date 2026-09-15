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
public class RoleDto {

    private Long id;

    private String name;

    private String description;

    private Long enterpriseId;

    private List<Long> departmentIds;

    private List<Long> userIds;

    private Instant createdAt;
}
