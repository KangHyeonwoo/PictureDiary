package com.picture.diary.extract;

import com.picture.diary.extract.data.Geometry;
import com.picture.diary.extract.data.Picture;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public abstract class AbstractPictureExtractor {

    /**
     * 추출
     * @param userId
     *
     *  1. 사진 파일 목록 조회
     *  2. 좌표 정보 유무 체크
     *  3.
     */
    public void extract(String userId) {
        List<Picture> pictureList = this.findPictureListByUserId(userId);

        for(Picture picture : pictureList) {
            if(picture.hasMetadata()) {

            }
        }

    }

    protected abstract List<Picture> findPictureListByUserId(String userId);

    private boolean moveFile(String from, String to) {
        Path fromPath = Paths.get(from);
        Path toPath = Paths.get(to);
        CopyOption[] copyOptions = {};

        try {
            Files.move(fromPath, toPath, copyOptions);
            return true;

        } catch (IOException e) {
            log.error("Fail the move file. Move path [{} -> {}]", from, to);
            return false;
        }

    }

    private void save() {

    }


}
