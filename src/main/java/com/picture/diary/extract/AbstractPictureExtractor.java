package com.picture.diary.extract;

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

    public void extract(String userId) {

        List<Picture> pictureList = this.findPictureListByUserId(userId);
        for(Object o : pictureList) {

        }

    }

    protected abstract List<Picture> findPictureListByUserId(String userId);

    protected abstract void getMetadata();

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
