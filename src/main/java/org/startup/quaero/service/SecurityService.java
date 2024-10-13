package org.startup.quaero.service;

import org.startup.quaero.dto.security.SignInDto;
import org.startup.quaero.dto.security.SignUpDto;
import org.startup.quaero.dto.security.SuccessAuthDto;
import org.startup.quaero.dto.security.TokensDto;

public interface SecurityService {
    SuccessAuthDto signUp(SignUpDto dto);

    SuccessAuthDto signIn(SignInDto dto);

    TokensDto updateAccessTokens(String refreshToken);
}
