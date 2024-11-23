package org.startup.quaero.Mapper;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.startup.quaero.database.entities.User;
import org.startup.quaero.dto.profile.EmployeeInfoDto;

@Component
public class UserToEmployeeInfoDtoConverter implements Converter<User, EmployeeInfoDto> {

    @Override
    public EmployeeInfoDto convert(MappingContext<User, EmployeeInfoDto> context) {
        User source = context.getSource();
        return EmployeeInfoDto.builder()
                .id(source.getId())
                .name(source.getFirstName())
                .surname(source.getLastName())
                .email(source.getEmail())
                .phone(source.getPhone())
                .country(source.getCountry())
                .city(source.getCity())
                .position(source.getPosition())
                .description(source.getDescription())
                .additionalInfo(source.getAdditionalInfo())
                .build();
    }
}