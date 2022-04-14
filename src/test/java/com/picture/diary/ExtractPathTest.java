package com.picture.diary;

import com.picture.diary.extract.data.ExtractPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExtractPathTest {

    @Autowired
    ExtractPath extractPath;

    @Test
    @DisplayName("추출 대상 경로를 extract.properties 에서 조회한다.")
    void getExtractPath() {
        String targetPath = "C";
        String savedPath = "D";

        Assertions.assertThat(extractPath.getTargetFolderPath()).isEqualTo(targetPath);
        Assertions.assertThat(extractPath.getSavedFolderPath()).isEqualTo(savedPath);
    }
}
