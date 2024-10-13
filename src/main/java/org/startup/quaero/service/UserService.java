package org.startup.quaero.service;


import org.springframework.stereotype.Service;
import org.startup.quaero.dto.UserDto;

import java.util.Optional;

public interface UserService {
    Optional<UserDto> findUserByEmail(String email);
}
