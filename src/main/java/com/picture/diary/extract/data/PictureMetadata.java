package com.picture.diary.extract.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class PictureMetadata {

    private String fileName;
    private String description;
    
    private String sggCode;
    private Geometry geometry;
    
    LocalDateTime pictureDate;

    @Builder
    public PictureMetadata(String fileName, String description, String sggCode, Geometry geometry, LocalDateTime pictureDate) {
    	this.fileName = fileName;
    	this.description = description;
    	this.sggCode = sggCode;
    	this.geometry = geometry;
    	this.pictureDate = pictureDate;
    }
}
