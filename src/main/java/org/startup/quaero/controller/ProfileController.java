package org.startup.quaero.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.startup.quaero.dto.profile.SetUserInfoDto;
import org.startup.quaero.dto.profile.UserInfoDto;
import org.startup.quaero.service.ProfileService;

@RestController("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "Отримання інформації про юзера")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    @GetMapping("/getUserInfo/{userId}")
    public ResponseEntity<UserInfoDto> getProfileInfo(
            @Parameter(description = "Id користувача, не має бути пустим") @PathVariable long userId){
        return ResponseEntity.ok(profileService.getUserInfo(userId));
    }

    @Operation(summary = "Оновлення інформації про юзера")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    })
    @PatchMapping("/setUserInfo/{userId}")
    public ResponseEntity<UserInfoDto> setUserInfo(@RequestBody SetUserInfoDto setUserInfoDto,
            @Parameter(description = "Id котрисувача, не має бути пустим") @PathVariable long userId) {
        UserInfoDto userInfoDto = profileService.setUserInfo(setUserInfoDto, userId);
        return ResponseEntity.ok(userInfoDto);
    }
}