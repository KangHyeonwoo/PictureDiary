package com.picture.diary.login.data;

import com.picture.diary.common.connection.response.ConnectionResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponseEntity extends ConnectionResponse {

    private String did;
    private boolean isPortalPort;
    private String sid;

}
