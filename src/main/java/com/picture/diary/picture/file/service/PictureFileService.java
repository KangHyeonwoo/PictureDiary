package com.picture.diary.picture.file.service;

import com.picture.diary.picture.file.data.Picture;

import java.util.List;
import java.util.Optional;

public interface PictureFileService {

    List<Picture> findListByUserId(String userId);

    //Optional<Picture> findByPictureName(String pictureName);

    boolean updateMetadata(Picture picture);
}
