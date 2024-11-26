package org.startup.quaero.dto.profile;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Builder
public class SetUserInfoDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private String city;
    private String position;
    private String description;
    private String companyName;
    private List<String> additionalInfo;
    private String role;
}
