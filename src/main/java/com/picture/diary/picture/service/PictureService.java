package com.picture.diary.picture.service;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.result.ResultList;

import java.util.List;

public interface PictureService {

    ResultList<PictureDto> findPictureList();

    ResultList<PictureDto> pictureExtract();
}
