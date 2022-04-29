package com.picture.diary;

import com.picture.diary.picture.file.data.Picture;
import com.picture.diary.picture.file.service.PictureFileService;
import com.picture.diary.picture.file.service.nas.NasPictureFileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class NasConnectTest {

    @Autowired
    PictureFileService pictureFileService;

    @Test
    @DisplayName("NAS 연결 잘 됐는지 테스트")
    void nasDriveConnectTest() throws IOException {
        String dir = "Z:/homes/hwkang/Photos/MobileBackup/iPhone (2)";

        Path path = Paths.get(dir);
        List<Path> paths = Files.walk(path).collect(Collectors.toList());

        Assertions.assertThat(paths.size()).isGreaterThan(0);
    }

    @Test
    void fileToPicture() throws IOException {
        List<Picture> fileList = pictureFileService.findListByUserId("hwkang");

        fileList.forEach(System.out::println);
    }


}
