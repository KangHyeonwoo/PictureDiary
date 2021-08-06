package com.picture.diary.picture.service;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureLocationDto;
import com.picture.diary.picture.data.PictureRenameDto;

import java.util.List;

public interface PictureService {

	PictureDto findByPictureId(long pictureId);

	List<PictureDto> findAllPictureList();

	PictureDto rename(PictureRenameDto pictureRenameDto);
	
	PictureDto updateLocation(PictureLocationDto pictureLocationDto);
	
	PictureDto updateAddress(PictureLocationDto pictureLocationDto);
	
	//PictureDto updateGeometry(long pictureId, double latitude, double longitude);

	void delete(long pictureId);

	PictureDto save(PictureDto pictureDto);
}
