package com.picture.diary.picture.service;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureLocationDto;
import com.picture.diary.picture.data.PictureRenameDto;

import java.util.List;

public interface PictureService {

	List<PictureDto> findListByUserId(String userId);

	PictureDto save(PictureDto pictureDto);

	void updateDescription(PictureDto pictureDto);

	void updateGeometry(PictureDto pictureDto);

	void delete(long pictureId);

}
