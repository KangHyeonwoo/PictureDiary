package com.picture.diary;

import com.picture.diary.extract.data.PicturePathProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FilePathTest {

    @Autowired
    PicturePathProperties filePathProperties;

    @Test
    void findFilePath() {

        String fromPath = filePathProperties.getFromPath();;
        String toPath = filePathProperties.getDataPath();
        String tempPath = filePathProperties.getTempPath();

        Assertions.assertThat(tempPath).isEqualTo("/download/temp/");
        Assertions.assertThat(fromPath).isEqualTo("/download/");
        Assertions.assertThat(toPath).isEqualTo("/download/save/");
    }
}
