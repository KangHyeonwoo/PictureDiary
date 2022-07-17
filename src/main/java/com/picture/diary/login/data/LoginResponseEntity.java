package com.picture.diary.login.data;

import com.picture.diary.common.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class LoginResponseEntity extends SuccessResponse {

    private String did;
    private boolean isPortalPort;
    private String sid;

    public static LoginResponseEntity of(Map<String, Object> dataMap) {
        String did = (String) dataMap.get("did");
        boolean isPortalPort  = (Boolean) dataMap.get("is_portal_port");
        String sid = (String) dataMap.get("sid");

        return new LoginResponseEntity(did, isPortalPort, sid);
    }
}
