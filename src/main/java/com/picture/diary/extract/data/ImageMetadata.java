package com.picture.diary.extract.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ImageMetadata {

    private String fileName;
    private String description;

    private double latitude;
    private double longitude;
}
