package org.startup.quaero.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.startup.quaero.database.entities.User;
import org.startup.quaero.database.repos.UserRepo;
import org.startup.quaero.dto.profile.EmployeeInfoDto;
import org.startup.quaero.dto.profile.HRInfoDto;
import org.startup.quaero.dto.profile.SetUserInfoDto;
import org.startup.quaero.dto.profile.UserInfoDto;
import org.startup.quaero.enums.Role;
import org.startup.quaero.exceptions.UserNotFoundException;
import org.startup.quaero.service.ProfileService;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepo userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserInfoDto getUserInfo(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return switch (user.getRole()) {
            case ROLE_RECRUITER -> modelMapper.map(user, HRInfoDto.class);
            case ROLE_EMPLOYEE -> modelMapper.map(user, EmployeeInfoDto.class);
            default -> throw new IllegalArgumentException("Unsupported role: " + user.getRole());
        };
    }

    @Override
    public UserInfoDto setUserInfo(SetUserInfoDto setUserDto, long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        modelMapper.map(setUserDto, existingUser);
        if (setUserDto.getRole() != null) {
            existingUser.setRole(Role.valueOf(setUserDto.getRole()));
        }
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserInfoDto.class);
    }

    @Override
    public String getUserRole(long userId){
        return userRepository.findRoleById(userId).toString();
    }
}
