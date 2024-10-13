package org.startup.quaero.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.startup.quaero.consts.ErrorMessages;
import org.startup.quaero.database.entities.User;
import org.startup.quaero.database.repos.UserRepo;
import org.startup.quaero.dto.UserDto;
import org.startup.quaero.dto.security.SignInDto;
import org.startup.quaero.dto.security.SignUpDto;
import org.startup.quaero.dto.security.SuccessAuthDto;
import org.startup.quaero.dto.security.TokensDto;
import org.startup.quaero.enums.Role;
import org.startup.quaero.exceptions.BadTokenException;
import org.startup.quaero.exceptions.UserNasAlreadyRegisteredException;
import org.startup.quaero.exceptions.WrongEmailException;
import org.startup.quaero.exceptions.WrongPasswordException;
import org.startup.quaero.security.jwt.JwtUtils;
import org.startup.quaero.service.SecurityService;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    public SuccessAuthDto signUp(SignUpDto dto) {
        User user = createNewRegisteredUser(dto);
        try {
            User savedUser = userRepo.save(user);
            user.setId(savedUser.getId());
        } catch (DataIntegrityViolationException e) {
            throw new UserNasAlreadyRegisteredException(ErrorMessages.USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
        }
        String accessToken = jwtUtils.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtils.createRefreshToken(modelMapper.map(user, UserDto.class));
        return SuccessAuthDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public SuccessAuthDto signIn(SignInDto dto) {
        User user = userRepo.findByEmail(dto.getEmail()).orElse(null);
        if (user == null) {
            throw new WrongEmailException(ErrorMessages.USER_NOT_FOUND_BY_EMAIL + " " + dto.getEmail());
        }
        if (!isPasswordCorrect(dto, user.getPassword())) {
            throw new WrongPasswordException(ErrorMessages.BAD_PASSWORD);
        }

        String accessToken = jwtUtils.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtils.createRefreshToken(modelMapper.map(user, UserDto.class));
        return SuccessAuthDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public TokensDto updateAccessTokens(String refreshToken) {
        String email;
        try {
            email = jwtUtils.getEmailOutOfAccessToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new BadTokenException(ErrorMessages.REFRESH_TOKEN_NOT_VALID);
        }
        UserDto user = modelMapper.map(userRepo.findByEmail(email), UserDto.class);

        if (jwtUtils.isTokenValid(refreshToken, JwtUtils.REFRESH_TOKEN_KEY)) {
            return new TokensDto(
                    jwtUtils.createAccessToken(user.getEmail(), user.getRole()),
                    jwtUtils.createRefreshToken(user));
        }
        throw new BadTokenException(ErrorMessages.REFRESH_TOKEN_NOT_VALID);
    }

    private boolean isPasswordCorrect(SignInDto signInDto, String password) {
        if (password == null) {
            return false;
        }
        return passwordEncoder.matches(signInDto.getPassword(), password);
    }


    private User createNewRegisteredUser(SignUpDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .dateOfRegistration(LocalDateTime.now())
                .role(Role.valueOf(dto.getRole()))
                .build();
    }
}