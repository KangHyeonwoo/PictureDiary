package com.picture.diary.extract.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * application.properties > file.path.*
 */
@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("file.path")
public class PicturePathProperties {

    /**
     * 원본 파일 경로
     */
    private final String fromPath;

    /**
     * 메타데이터가 존재하는 파일 저장 경로
     */
    private final String dataPath;

    /**
     * 삭제된 파일의 경로
     */
    private final String deletePath;
    
    
    public String getFromPath(String fileName, Extensions extension) {
    	
    	return this.fromPath + "/" + fileName + "." + extension;
    }
    
    public String getDataPath(String fileName, Extensions extension) {
    	
    	return this.dataPath + "/" + fileName + "." + extension;
    }
    
    public String getDeletePath(String fileName, Extensions extension) {
    	
    	return this.deletePath + "/" + fileName + "." + extension;
    }
}
