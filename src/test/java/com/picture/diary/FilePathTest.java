package com.picture.diary;

import com.picture.diary.extract.data.FilePathProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FilePathTest {

    @Autowired
    FilePathProperties filePathProperties;

    @Test
    void findFilePath() {

        String beforePath = filePathProperties.getBeforePath();;
        String afterPath = filePathProperties.getAfterPath();
        String tempPath = filePathProperties.getTempPath();

        Assertions.assertThat(tempPath).isEqualTo("/download/temp/");
        Assertions.assertThat(beforePath).isEqualTo("/download/");
        Assertions.assertThat(afterPath).isEqualTo("/download/save/");
    }
}
