package com.picture.diary.login.data;

import com.picture.diary.common.response.SuccessResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponseEntity extends SuccessResponse {

    private String did;
    private boolean isPortalPort;
    private String sid;

}
