package com.picture.diary.picture.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PictureLocationDto {
	
	@NotNull(message = "사진 아이디는 필수값입니다.")
	private long pictureId;
	
	@NotNull @NotEmpty
	private double latitude;
	
	@NotNull @NotEmpty
	private double longitude;
	
	@NotBlank(message = "주소를 확인해주세요.")
	@Size(max = 200, message = "주소는 200자를 초과할 수 없습니다.")
	private String address;
	
	@Builder
	public PictureLocationDto(long pictureId, double latitude, double longitude, String address) {
		this.pictureId = pictureId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
	}
}
