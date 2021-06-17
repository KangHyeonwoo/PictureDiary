package com.picture.diary.extract.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.File;

@Builder
@AllArgsConstructor
@Getter @ToString
public class PictureFile {
	
	/**
	 * 파일명 (확장자 제외)
	 */
    private String fileName;
    
    /**
     * 확장자
     */
    private Extensions extension;
    
    /**
     * 파일 크기
     */
    private long fileSize;

    /**
     * 파일 경로 (파일명 제외)
     */
    private String filePath;
    
    /**
     * 메타데이터
     */
    private PictureMetadata pictureMetadata;
    
    public PictureFile(File file) {
        String fileName = file.getName();
        
        this.fileName = file.getName().split(SplitParts.DOT.getValue())[0];
        this.extension = Extensions.findOf(fileName);
        this.fileSize = file.length();
        this.filePath = file.getPath();
    }
    
    public void addMetadata(PictureMetadata pictureMetadata) {
    	this.pictureMetadata = pictureMetadata;
    }
    
    public void changeFilePath(String path) {
    	this.filePath = path;
    }
    
    
}
