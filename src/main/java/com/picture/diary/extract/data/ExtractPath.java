package com.picture.diary.extract.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 *  파일 추출 경로 클래스
 *   - extract.properties 참조
 */
@Getter
@Configuration
public class ExtractPath {

    @Value("${extract.target.folder.path}")
    private String targetFolderPath;

    @Value("${extract.saved.folder.path}")
    private String savedFolderPath;
}
