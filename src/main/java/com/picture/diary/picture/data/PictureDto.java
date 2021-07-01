package com.picture.diary.picture.data;

import com.picture.diary.extract.data.Extensions;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
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

    private boolean hasGeometry;
    private String tocId;
    
    @Builder
    public PictureDto(long pictureId, String pictureName, String pictureOriginName, Extensions extension, long pictureSize,
                      String picturePath, LocalDateTime pictureDate, double latitude, double longitude,
                      LocalDateTime createDt, LocalDateTime updateDt) {
        this.pictureId =pictureId;
        this.pictureName = pictureName;
        this.pictureOriginName = pictureOriginName;
        this.extension = extension;
        this.pictureSize = pictureSize;
        this.picturePath = picturePath;
        this.pictureDate = pictureDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createDt = createDt;
        this.updateDt = updateDt;
        
        this.hasGeometry = (this.latitude > 0 && this.longitude > 0);
        this.tocId = this.hasGeometry ? "temp-group_" + this.pictureId : "data-group_" + this.pictureId;
    }

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
