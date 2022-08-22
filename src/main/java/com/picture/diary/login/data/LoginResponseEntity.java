package com.picture.diary.login.data;

import com.picture.diary.common.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class LoginResponseEntity extends SuccessResponse {

    private LoginType loginType;
    private String userId;
    private LocalDateTime loginTime;

    /*
    //너무 NAS 의존적이어서 삭제함.
    private String did;
    private boolean isPortalPort;
    private String sid;

    public static LoginResponseEntity of(Map<String, Object> dataMap) {
        String did = (String) dataMap.get("did");
        boolean isPortalPort  = (Boolean) dataMap.get("is_portal_port");
        String sid = (String) dataMap.get("sid");

        return new LoginResponseEntity(did, isPortalPort, sid);
    }
    */

}
