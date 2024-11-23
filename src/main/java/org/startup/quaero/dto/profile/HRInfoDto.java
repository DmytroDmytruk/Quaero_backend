package org.startup.quaero.dto.profile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@SuperBuilder
public class HRInfoDto extends UserInfoDto{
    private String companyName;
    private List<String> additionalInfo;
}
