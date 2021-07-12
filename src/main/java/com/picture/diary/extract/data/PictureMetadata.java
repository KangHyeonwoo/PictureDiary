package com.picture.diary.extract.data;

import java.time.LocalDateTime;

import com.drew.metadata.Metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PictureMetadata {

	Metadata metadata;
    private Geometry geometry;
    LocalDateTime pictureDate;
    
    @Builder
    public PictureMetadata(Metadata metadata, Geometry geometry, LocalDateTime pictureDate) {
    	this.metadata = metadata;
    	this.geometry = geometry;
    	this.pictureDate = pictureDate;
    }
}
