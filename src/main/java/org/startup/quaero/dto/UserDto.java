package org.startup.quaero.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.startup.quaero.enums.Role;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UserDto {
    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private String password;
    private Role role;
}
