package org.startup.quaero.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.startup.quaero.dto.security.SignInDto;
import org.startup.quaero.dto.security.SignUpDto;
import org.startup.quaero.dto.security.SuccessAuthDto;
import org.startup.quaero.dto.security.TokensDto;
import org.startup.quaero.service.SecurityService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class SecurityController {

    private final SecurityService securityService;

    @Operation(summary = "реєстрація користувача")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    @PostMapping("/signUp")
    public ResponseEntity<SuccessAuthDto> singUp(@Valid @RequestBody SignUpDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(securityService.signUp(dto));
    }

    @Operation(summary = "авторизація користувача")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    @PostMapping("/signIn")
    public ResponseEntity<SuccessAuthDto> singIn(@Valid @RequestBody SignInDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(securityService.signIn(dto));
    }

    @Operation(summary = "оновлення jwt токену")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    @GetMapping("/updateAccessToken")
    public ResponseEntity<TokensDto> updateAccessToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok().body(securityService.updateAccessTokens(refreshToken));
    }
}
