package com.picture.diary.picture.file.data;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 *  파일 추출 경로 클래스
 *   - nas.properties 참조
 */
@Getter
@Configuration
public class NasProperty {

    @Value("${picture.path}")
    private String picturePath;

}
