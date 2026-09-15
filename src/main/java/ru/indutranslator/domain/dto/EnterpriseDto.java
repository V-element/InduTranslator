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
public class EnterpriseDto {

    private Long id;

    private String name;

    private String description;

    private String logoUrl;

    private String website;

    private String address;

    private String phone;

    private String email;

    private Boolean active;

    private Instant createdAt;
}
