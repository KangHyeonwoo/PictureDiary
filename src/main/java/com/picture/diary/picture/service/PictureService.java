package com.picture.diary.picture.service;

import com.picture.diary.picture.data.InfowindowDto;
import com.picture.diary.picture.data.PictureDto;

import java.util.List;

public interface PictureService {

	PictureDto findByPictureId(long pictureId);

	List<PictureDto> findPictureList();

	List<PictureDto> pictureExtract();

	PictureDto rename(long pictureId, String pictureName);

	PictureDto updateGeometry(long pictureId, double latitude, double longitude);

	void delete(long pictureId);

	PictureDto save(PictureDto pictureDto);
	
	InfowindowDto findInfowindowByPictureId(long pictureId);
}
