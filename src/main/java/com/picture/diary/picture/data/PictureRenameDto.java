package com.picture.diary.picture.data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PictureRenameDto {
	
	@NotNull
	private long pictureId;
	
	@NotNull
	@Size(max = 15)
	private String pictureName;
	
	@Builder
	public PictureRenameDto(long pictureId, String pictureName) {
		this.pictureId = pictureId;
		this.pictureName = pictureName;
	}
}
