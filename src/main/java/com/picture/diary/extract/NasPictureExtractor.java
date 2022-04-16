package com.picture.diary.extract;

import com.picture.diary.extract.data.ExtractPath;
import com.picture.diary.extract.data.Picture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NasPictureExtractor extends AbstractPictureExtractor {

    private final ExtractPath extractPath;

    @Override
    protected List<Picture> findPictureListByUserId(String userId) {
        List<Picture> pictureList = new ArrayList<>();
        String path = extractPath.getTargetFolderPath() + "/" + userId;

        return pictureList;
    }

    @Override
    protected void getMetadata() {

    }
}
