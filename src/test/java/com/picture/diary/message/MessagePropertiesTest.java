package com.picture.diary.message;

import com.picture.diary.common.provider.ApplicationContextProvider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;

@SpringBootTest
public class MessagePropertiesTest {

    @Test
    void 어플리케이션_빈_조회_실패() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ApplicationContextProvider.getApplicationContext().getBean(MessageSourceAccessor.class);
        });

        /*
            https://mangkyu.tistory.com/125
            @see https://stackoverflow.com/questions/5133291/applicationcontextprovider-is-not-being-called

            ApplicationContextProvider 인스턴스를 가져올 수 있는 시점은
            어플리케이션이 구동이 완료된 후, 즉 모든 스프링 빈 객체가 만들어 진 이후이다.

            위와 같은 이유로 스프링 빈을 만드는 동안(config 등의 파일 읽는 중)에는 ApplicationContextProvider.getApplicationContext() 메소드가 null 일 수 있기 때문에
            해당 메소드 사용을 지양해야 한다.

            이러한 경우(정말 어쩔 수 없는 경우)에는 @Autowired 어노테이션을 사용할 수 있고, 이럴 때 사용하라고 있는 어노테이션이 @Autowired 이다.
            물론 더 좋은 방법이 있을 수 있고 계속 찾아보겠지만, 우선은 @Autowired 어노테이션을 사용해야겠다.
         */
    }

    @Autowired
    MessageSourceAccessor messageSourceAccessor;

    @Test
    void 메시지_조회() {
        String message = messageSourceAccessor.getMessage("login.success");
        org.assertj.core.api.Assertions.assertThat(message).isEqualTo("로그인에 성공하였습니다.");
    }
}
