package org.startup.quaero.service;


import org.startup.quaero.dto.profile.SetUserInfoDto;
import org.startup.quaero.dto.profile.UserInfoDto;

public interface ProfileService {
    UserInfoDto getUserInfo(long userId);
    UserInfoDto setUserInfo(SetUserInfoDto setUserDto, long userId);
}
