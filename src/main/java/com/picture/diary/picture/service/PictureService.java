package com.picture.diary.picture.service;

import com.picture.diary.picture.data.PictureDto;

import java.util.List;

public interface PictureService {

	List<PictureDto> findNotDeletedListByUserId(String userId);

	List<PictureDto> findAllListByUserId(String userId);

	/**
	 *  TODO 안 필요할 것 같긴 함.
	 */
	@Deprecated
	PictureDto save(PictureDto pictureDto);

	void updateDescription(PictureDto pictureDto);

	void updateGeometry(PictureDto pictureDto);

	void delete(long pictureId);

}
