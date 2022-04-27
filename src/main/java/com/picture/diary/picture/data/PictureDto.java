package com.picture.diary.picture.data;

import com.picture.diary.picture.file.data.Extensions;
import com.picture.diary.picture.file.data.Geometry;
import com.picture.diary.picture.file.data.Picture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class PictureDto {

    private long pictureId;

    //확장자 및 경로를 제외한 파일 명
    private String pictureName;

    //파일 확장자
    private Extensions extension;

    //파일 크기
    private long size;

    //파일 생성 일시
    private LocalDateTime dateTime;

    //좌표
    private Geometry geometry;

    //삭제 여부
    private String deleteAt;

    //등록자 ID
    private String userId;

    //파일 설명
    private String description;

    //DB 저장 시간
    private LocalDateTime createDt;

    //DB 최종 수정 시간
    private LocalDateTime updateDt;
    
    @Builder
    public PictureDto(long pictureId, String pictureName, Extensions extension, long size, LocalDateTime dateTime,
    		Geometry geometry, String deleteAt, String userId, String description, LocalDateTime createDt, LocalDateTime updateDt) {
        this.pictureId =pictureId;
        this.pictureName = pictureName;
        this.extension = extension;
        this.size = size;
        this.dateTime = dateTime;
        this.geometry = geometry;
        this.deleteAt = deleteAt;
        this.userId = userId;
        this.description = description;
        this.createDt = createDt;
        this.updateDt = updateDt;
    }

    public void rename(String pictureName) {
        this.pictureName = pictureName;
    }

    public void updateLocation(String address, double latitude, double longitude) {
    	this.geometry = new Geometry(latitude, longitude);
    }

    public PictureEntity toEntity() {
        return PictureEntity.builder()
                .pictureId(this.pictureId)
                .pictureName(this.pictureName)
                .extension(this.extension)
                .size(this.size)
                .dateTime(this.dateTime)
                .geometry(this.geometry)
                .deleteAt(this.deleteAt)
                .userId(this.userId)
                .description(this.description)
                .build();
    }

    public static PictureDto createBy(String userId, Picture picture) {
        return PictureDto.builder()
                .pictureName(picture.getName())
                .extension(picture.getExtension())
                .size(picture.getSize())
                .dateTime(picture.getLocalDateTime())
                .geometry(picture.getGeometry())
                .deleteAt("N")
                .userId(userId)
                .build();

    }
}
