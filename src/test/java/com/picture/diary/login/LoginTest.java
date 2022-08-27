package com.picture.diary.login;

import com.picture.diary.common.response.BasicResponse;
import com.picture.diary.common.response.SuccessResponse;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.data.LoginResponseEntity;
import com.picture.diary.login.data.LoginType;
import com.picture.diary.login.service.LoginService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

@SpringBootTest
public class LoginTest {

    @Autowired
    private LoginService loginService;


    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("유효성 검사) 아이디, 비밀번호가 정상적으로 입력된 경우 테스트에서 성공한다.")
    void success() {
        //1. given 정상적인 아이디, 비밀번호 부여하고
        final String id = "test01";
        final String password = "Test1234!";

        LoginRequestDto loginRequestDto = new LoginRequestDto(LoginType.SYNOLOGY_NAS, id, password);

        //2. when 유효성 검증하면
        Set<ConstraintViolation<LoginRequestDto>> validate = validator.validate(loginRequestDto);

        //3. then 유효성에 맞지 않는 결과 건수가 0이 나온다.
        Assertions.assertThat(validate.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("유효성 검사) 아이디가 4글자 이하인 경우 테스트에 실패한다.")
    void loginRequestDtoValidationFail() {
        final String id = "a";
        final String password = "Test1234!";

        LoginRequestDto loginRequestDto = new LoginRequestDto(LoginType.SYNOLOGY_NAS, id, password);

        //2. when 유효성 검증하면
        Set<ConstraintViolation<LoginRequestDto>> validate = validator.validate(loginRequestDto);

        validate.stream().forEach(violation -> {
            System.out.println(violation.getMessage());
        });
    }

    @Test
    @DisplayName("Synology API) 로그인 성공")
    void login_success() throws IOException {
        final String id = "hwkang";
        final String password = "Rla0011!!!";

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .userId(id)
                .password(password)
                .build();

        LoginResponseEntity responseEntity = loginService.login(loginRequestDto);

        System.out.println(responseEntity.isSuccess());
        //System.out.println(responseEntity.getSid());
    }

}
