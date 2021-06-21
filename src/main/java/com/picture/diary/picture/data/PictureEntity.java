package com.picture.diary.picture.data;

import com.picture.diary.extract.data.Extensions;
import com.picture.diary.extract.data.Geometry;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "picture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class PictureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long pictureId;

    @Column(length = 100)
    private String pictureName;

    @Column(nullable = false, length = 20)
    private String pictureOriginName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Extensions extension;

    @Column(nullable = false)
    private long pictureSize;

    @Column(nullable = false)
    private String picturePath;

    @Column
    private LocalDateTime pictureDate;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @CreationTimestamp
    @Column
    private LocalDateTime createDt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updateDt;

    @Builder
    public PictureEntity(long pictureId, String pictureName, String pictureOriginName, Extensions extension,
                         long pictureSize, String picturePath, LocalDateTime pictureDate, Geometry geometry) {
        this.pictureId = pictureId;
        this.pictureName = pictureName;
        this.pictureOriginName = pictureOriginName;
        this.extension = extension;
        this.pictureSize = pictureSize;
        this.picturePath = picturePath;
        this.pictureDate = pictureDate;

        if(geometry != null) {
            this.latitude = geometry.getLatitude();
            this.longitude = geometry.getLongitude();
        }
    }

    public PictureDto toDto() {
        return PictureDto.builder()
                .pictureId(this.pictureId)
                .pictureName(this.pictureName)
                .pictureOriginName(this.pictureOriginName)
                .extension(this.extension)
                .pictureSize(this.pictureSize)
                .picturePath(this.picturePath)
                .pictureDate(this.pictureDate)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .createDt(this.createDt)
                .updateDt(this.updateDt)
                .build();
    }
}
