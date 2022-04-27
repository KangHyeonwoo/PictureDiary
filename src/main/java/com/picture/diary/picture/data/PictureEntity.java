package com.picture.diary.picture.data;

import com.picture.diary.picture.file.data.Extensions;
import com.picture.diary.picture.file.data.Geometry;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "picture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class PictureEntity {

    /*
        파일 경로는 고정.
        user_id 필요  notnull maxlength 20
        description 필요 nullable
        deleteAt

        pictureOriginName -> 필요없음
        address -> 필요없음
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long pictureId;

    @Column(length = 100)
    private String pictureName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Extensions extension;

    @Column(nullable = false)
    private long size;   // -> size

    @Column
    private LocalDateTime dateTime;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column(nullable = false)
    private String deleteAt;

    @Column(nullable = false)
    private String userId;

    @Column
    private String description;

    @CreationTimestamp
    @Column
    private LocalDateTime createDt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updateDt;

    @Builder
    public PictureEntity(long pictureId, String pictureName, Extensions extension, long size, LocalDateTime dateTime,
                         Geometry geometry, String deleteAt, String userId, String description) {

        this.pictureId = pictureId;
        this.pictureName = pictureName;
        this.extension = extension;
        this.size = size;
        this.dateTime = dateTime;
        if(geometry != null) {
            this.latitude = geometry.getLatitude();
            this.longitude = geometry.getLongitude();
        }
        this.deleteAt = deleteAt;
        this.userId = userId;
        this.description = description;
    }

    public PictureDto toDto() {
        return PictureDto.builder()
                .pictureId(this.pictureId)
                .pictureName(this.pictureName)
                .extension(this.extension)
                .size(this.size)
                .dateTime(this.dateTime)
                .geometry(new Geometry(this.latitude, this.longitude))
                .deleteAt(this.deleteAt)
                .userId(this.userId)
                .description(this.description)
                .createDt(this.createDt)
                .updateDt(this.updateDt)
                .build();
    }

    public boolean isNotDeleted() {
        return "N".equals(this.deleteAt);
    }
}
