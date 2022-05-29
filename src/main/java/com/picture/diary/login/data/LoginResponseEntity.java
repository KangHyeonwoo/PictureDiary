package com.picture.diary.login.data;

import com.picture.diary.common.connection.response.ConnectionResponseEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponseEntity extends ConnectionResponseEntity {

    private String did;
    private boolean isPortalPort;
    private String sid;

}
