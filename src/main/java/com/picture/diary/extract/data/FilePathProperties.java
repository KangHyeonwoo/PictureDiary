package com.picture.diary.extract.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

/**
 * application.properties > file.path.*
 */
@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("file.path")
public class FilePathProperties {

    /**
     * 원본 파일 경로
     */
    private final String beforePath;

    /**
     * 메타데이터가 존재하는 파일 저장 경로
     */
    private final String afterPath;

    /**
     * 메타데이터가 존재하지 않는 파일의 저장 경로
     */
    private final String tempPath;


}
