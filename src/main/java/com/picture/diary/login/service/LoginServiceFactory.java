package com.picture.diary.login.service;

import com.picture.diary.common.provider.ApplicationContextProvider;
import com.picture.diary.login.data.LoginType;
import com.picture.diary.login.service.impl.NasLoginService;
import org.springframework.context.ApplicationContext;

/**
 * LoginService 생성 팩토리
 *  @default : SYNOLOGY_NAS
 */
public class LoginServiceFactory {
    //생성자 제한
    private LoginServiceFactory(){};

    public static LoginService of(LoginType loginType) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        LoginService loginService = applicationContext.getBean(NasLoginService.class);

        switch (loginType) {
            case SYNOLOGY_NAS:
                loginService = applicationContext.getBean(NasLoginService.class);
                break;
        }

        return loginService;
    }
}
