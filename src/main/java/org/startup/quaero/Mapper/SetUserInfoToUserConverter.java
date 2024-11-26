package org.startup.quaero.Mapper;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.startup.quaero.database.entities.User;
import org.startup.quaero.dto.profile.SetUserInfoDto;
import org.startup.quaero.enums.Role;
import java.time.LocalDateTime;


public class SetUserInfoToUserConverter implements Converter<SetUserInfoDto, User> {
    @Override
    public User convert(MappingContext<SetUserInfoDto, User> mappingContext) {
        SetUserInfoDto source = mappingContext.getSource();

        Role role = Role.valueOf(source.getRole().toUpperCase());

        return User.builder()
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .email(source.getEmail())
                .role(role)
                .dateOfRegistration(LocalDateTime.now())
                .phone(source.getPhone())
                .country(source.getCountry())
                .city(source.getCity())
                .position(source.getPosition())
                .description(source.getDescription())
                .additionalInfo(source.getAdditionalInfo())
                .companyName(source.getCompanyName())
                .build();
    }
}
