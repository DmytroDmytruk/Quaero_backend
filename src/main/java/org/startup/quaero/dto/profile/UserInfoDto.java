package org.startup.quaero.dto.profile;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Data
@Getter
@SuperBuilder
public class UserInfoDto {
    private long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String country;
    private String city;
}
