package com.picture.diary.extract.data;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PictureMetadata {

    private Geometry geometry;
    LocalDateTime pictureDate;
    
    @Builder
    public PictureMetadata(Geometry geometry, LocalDateTime pictureDate) {
    	this.geometry = geometry;
    	this.pictureDate = pictureDate;
    }
}
