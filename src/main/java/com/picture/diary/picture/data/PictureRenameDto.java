package com.picture.diary.picture.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PictureRenameDto {
	
	@NotNull(message = "사진 아이디는 필수값입니다.")
	private long pictureId;
	
	@NotBlank(message = "사진명은 필수값입니다.")
	@Size(max = 15, message = "사진명은 15자를 초과할 수 없습니다.")
	private String pictureName;
	
	@Builder
	public PictureRenameDto(long pictureId, String pictureName) {
		this.pictureId = pictureId;
		this.pictureName = pictureName;
	}
}
