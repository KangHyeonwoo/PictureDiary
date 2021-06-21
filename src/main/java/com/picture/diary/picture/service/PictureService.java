package com.picture.diary.picture.service;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.result.Result;

import java.util.List;

public interface PictureService {

	Result<List<PictureDto>> findPictureList();

	Result<List<PictureDto>> pictureExtract();
}
