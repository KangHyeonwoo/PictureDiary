package com.picture.diary.extract.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class ImageMetadata {

    private String fileName;
    private String description;

    private String sggCode;

    private double latitude;
    private double longitude;

    LocalDateTime date;
}
