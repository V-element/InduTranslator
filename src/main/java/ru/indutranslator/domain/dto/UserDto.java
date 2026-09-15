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
public class UserDto {

    private Long id;

    private Long enterpriseId;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Boolean active;

    private String password;

    private List<Long> departmentIds;

    private List<Long> roleIds;

    private Instant createdAt;
}
