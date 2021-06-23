package com.picture.diary.picture.service;

import com.picture.diary.picture.data.PictureDto;

import java.util.List;

public interface PictureService {

	PictureDto findByPictureId(long pictureId);

	List<PictureDto> findPictureList();

	List<PictureDto> pictureExtract();
}
