package com.picture.diary.picture.file.service.nas;

import com.picture.diary.picture.file.data.Extensions;
import com.picture.diary.picture.file.data.NasProperty;
import com.picture.diary.picture.file.data.Picture;
import com.picture.diary.picture.file.service.PictureFileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class NasPictureFileService implements PictureFileService {

    private final NasProperty nasProperty;

    @Override
    public List<Picture> findListByUserId(String userId) {
        String userDir = nasProperty.getPicturePath() + "/" + userId;
        Path path = Paths.get(userDir);
        List<Picture> pictureList = new ArrayList<>();

        try {
            pictureList = Files.walk(path)
                    .filter(file -> Extensions.contains(file.getFileName().toString()))
                    .map(file -> file.toFile())
                    .map(Picture::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("어떤 에러가 나옴");
            e.printStackTrace();
        }

        return pictureList;
    }

    /* @Override
    public Optional<Picture> findByPictureName(String pictureName) {


        return Optional.empty();
    }*/

    @Override
    public boolean updateMetadata(Picture picture) {
        try {
            //사진이 이미 복사본일 경우 따로 복사하지 않음
            Picture copyPicture = picture.isCopied() ? picture : this.copyPicture(picture);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 사진파일 복사
     *
     * @param picture 원본 사진
     * @return picture 복사본 사진
     */
    private Picture copyPicture(Picture picture) throws IOException {
        File fromFile = new File(picture.getFullPath());
        File toFile = new File(picture.getCopyFullPath());

        Path resultPath = Files.copy(fromFile.toPath(), toFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);

        return new Picture(resultPath.toFile());
    }
}
