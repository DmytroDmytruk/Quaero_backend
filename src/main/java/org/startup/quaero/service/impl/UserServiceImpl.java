package org.startup.quaero.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.startup.quaero.consts.ErrorMessages;
import org.startup.quaero.database.entities.User;
import org.startup.quaero.database.repos.UserRepo;
import org.startup.quaero.dto.UserDto;
import org.startup.quaero.exceptions.UserNotFoundException;
import org.startup.quaero.service.UserService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepo userRepo;

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessages.USER_NOT_FOUND_BY_EMAIL_ERROR));
        return Optional.of(modelMapper.map(user, UserDto.class));
    }
}
