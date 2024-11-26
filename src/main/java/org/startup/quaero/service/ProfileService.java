package org.startup.quaero.service;


import org.startup.quaero.dto.profile.SetUserInfoDto;
import org.startup.quaero.dto.profile.UserInfoDto;

import java.util.List;

public interface ProfileService {
    UserInfoDto getUserInfo(long userId);
    UserInfoDto setUserInfo(SetUserInfoDto setUserDto, long userId);
    String getUserRole(long userId);
    void setAboutMe(long userId, String aboutMe);
    void setAdditionalInfo(long userId, List<String> additionalInfo);
}
