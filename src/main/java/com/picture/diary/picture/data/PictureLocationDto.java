package com.picture.diary.picture.data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PictureLocationDto {
	
	@NotNull(message = "사진 아이디는 필수값입니다.")
	private long pictureId;
	
	@NotNull @NotEmpty
	private double latitude;
	
	@NotNull @NotEmpty
	private double longitude;
	
	private String address;
	
	@Builder
	public PictureLocationDto(long pictureId, double latitude, double longitude, String address) {
		this.pictureId = pictureId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
	}
}
