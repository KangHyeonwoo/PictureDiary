package com.picture.diary.picture.data;

import java.time.LocalDateTime;

import com.picture.diary.extract.data.Extensions;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InfowindowDto {
	private long pictureId;
	private String pictureName;
	private LocalDateTime pictureDate;
	private String pictureOriginName;
	private Extensions extension;
	
	@Builder
	public InfowindowDto(long pictureId, String pictureName, LocalDateTime pictureDate,
			String pictureOriginName, Extensions extension) {
		this.pictureId = pictureId;
		this.pictureName = pictureName;
		this.pictureDate = pictureDate;
		this.pictureOriginName = pictureOriginName;
		this.extension = extension;
	}
}
