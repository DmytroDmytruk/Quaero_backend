package org.startup.quaero.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuccessAuthDto {
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private String firstName;
    private String lastName;
}
