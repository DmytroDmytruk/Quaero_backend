package org.startup.quaero.Mapper;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.startup.quaero.database.entities.User;
import org.startup.quaero.dto.profile.HRInfoDto;

@Component
public class UserToHRInfoDtoConverter implements Converter<User, HRInfoDto> {

    @Override
    public HRInfoDto convert(MappingContext<User, HRInfoDto> context) {
        User source = context.getSource();
        return HRInfoDto.builder()
                .id(source.getId())
                .name(source.getFirstName())
                .surname(source.getLastName())
                .email(source.getEmail())
                .phone(source.getPhone())
                .country(source.getCountry())
                .city(source.getCity())
                .companyName(source.getCompanyName())
                .additionalInfo(source.getAdditionalInfo())
                .build();
    }
}
