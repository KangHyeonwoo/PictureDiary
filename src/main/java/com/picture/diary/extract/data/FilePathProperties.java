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
public class FilePathProperties {

    /**
     * 원본 파일 경로
     */
    private final String fromPath;

    /**
     * 메타데이터가 존재하는 파일 저장 경로
     */
    private final String dataPath;

    /**
     * 메타데이터가 존재하지 않는 파일의 저장 경로
     */
    private final String tempPath;

    
    public String getFromPath(ImageFile imageFile) {
    	
    	return this.fromPath + "/" + imageFile.getFileName() + "." + imageFile.getExtension();
    }
    
    public String getDataPath(ImageFile imageFile) {
    	
    	return this.dataPath + "/" + imageFile.getFileName() + "." + imageFile.getExtension();
    }
    
    public String getTempPath(ImageFile imageFile) {
    	
    	return this.tempPath + "/" + imageFile.getFileName() + "." + imageFile.getExtension();
    }
}
