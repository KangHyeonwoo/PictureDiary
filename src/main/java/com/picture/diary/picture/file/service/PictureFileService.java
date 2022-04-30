package com.picture.diary.picture.file.service;

import com.picture.diary.picture.file.data.Picture;

import java.util.List;
import java.util.Optional;

public interface PictureFileService {

    @Deprecated
    List<Picture> findListByUserId(String userId);

    List<Picture> findAllListByUserId(String userId);

    Picture findByPath(String path);
}
