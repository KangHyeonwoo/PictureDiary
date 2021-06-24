package com.picture.diary.picture.data;

import com.picture.diary.extract.data.Extensions;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class PictureDto {

    private long pictureId;
    private String pictureName;
    private String pictureOriginName;
    private Extensions extension;
    private long pictureSize;
    private String picturePath;
    private LocalDateTime pictureDate;
    private double latitude;
    private double longitude;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    public void rename(String pictureName) {
        this.pictureName = pictureName;
    }

    public void updateGeometry(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PictureEntity toEntity() {
        return PictureEntity.builder()
                .pictureId(this.pictureId)
                .pictureName(this.pictureName)
                .pictureOriginName(this.pictureOriginName)
                .extension(this.extension)
                .pictureSize(this.pictureSize)
                .picturePath(this.picturePath)
                .pictureDate(this.pictureDate)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }
}
