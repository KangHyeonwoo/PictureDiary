package com.picture.diary.picture.file.service.nas;

import com.picture.diary.picture.file.data.Extensions;
import com.picture.diary.utils.NasProperty;
import com.picture.diary.picture.file.data.Picture;
import com.picture.diary.picture.file.service.PictureFileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        //NAS 내의 사진 파일 중에서 Extensions 에 정의된 확장자를 가진 파일과, 좌표가 있는 데이터만 리턴한다.
        try {
            pictureList = Files.walk(path)
                    .filter(file -> Extensions.contains(file.getFileName().toString()))
                    .map(file -> file.toFile())
                    .map(file -> new Picture(file))
                    .filter(Picture::hasGeometry)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("message", e.getMessage());
        }

        return pictureList;
    }

    @Override
    public List<Picture> findAllListByUserId(String userId) {
        String userDir = nasProperty.getPicturePath() + "/" + userId;
        Path path = Paths.get(userDir);
        List<Picture> pictureList = new ArrayList<>();

        try {
            pictureList = Files.walk(path)
                    .filter(file -> Extensions.contains(file.getFileName().toString()))
                    .map(file -> file.toFile())
                    .map(file -> new Picture(file))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("message", e.getMessage());
        }

        return pictureList;
    }

    @Override
    public Picture findByPath(String path) {
        File file = new File(path);

        if(Extensions.contains(file.getName())) {
            return new Picture(file);
        }

        throw new InvalidFileNameException(file.getName(), "not allowed extensions");
    }

}
