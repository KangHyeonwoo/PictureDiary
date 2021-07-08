package com.picture.diary.extract.data;

import com.picture.diary.picture.data.PictureEntity;
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
     * 메타데이터
     */
    private PictureMetadata pictureMetadata;
    
    public PictureFile(File file) {
        String fileName = file.getName();
        
        this.fileName = file.getName().split(SplitParts.DOT.getValue())[0];
        this.extension = Extensions.findOf(fileName);
        this.fileSize = file.length();
    }
    
    public void addMetadata(PictureMetadata pictureMetadata) {
    	this.pictureMetadata = pictureMetadata;
    }
    
    public PictureEntity toEntity() {
        return PictureEntity.builder()
                .pictureOriginName(this.fileName)
                .extension(this.extension)
                .pictureSize(this.fileSize)
                .pictureDate(pictureMetadata.getPictureDate())
                .geometry(pictureMetadata.getGeometry())
                .build();
    }
}
