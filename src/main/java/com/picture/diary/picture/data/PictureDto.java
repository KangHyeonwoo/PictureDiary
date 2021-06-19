package com.picture.diary.picture.data;

import com.picture.diary.extract.data.Extensions;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
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

}
