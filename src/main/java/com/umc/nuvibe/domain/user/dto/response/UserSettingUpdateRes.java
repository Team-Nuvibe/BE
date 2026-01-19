package com.umc.nuvibe.domain.user.dto.response;


import com.umc.nuvibe.domain.user.vo.UserSetting;

import java.util.List;

public record UserSettingUpdateRes (UserSetting setting, List<String> changes) {
}
