package com.picture.diary;

import com.picture.diary.picture.file.data.NasProperty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PropertiesTest {

    @Autowired
    NasProperty extractPath;

    @Test
    @DisplayName("추출 대상 경로를 nas.properties 에서 조회한다.")
    void getExtractPath() {
        String comparePath = "C";

        Assertions.assertThat(extractPath.getPicturePath()).isEqualTo(comparePath);
    }
}
