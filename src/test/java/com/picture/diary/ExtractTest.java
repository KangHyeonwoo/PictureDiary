package com.picture.diary;

import com.picture.diary.extract.ImageFileExtractorServiceImpl;
import com.picture.diary.extract.data.FileData;
import com.picture.diary.extract.data.ImageMetadata;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class ExtractTest {

    @Autowired
    ImageFileExtractorServiceImpl dataExtractorService;

    @Test
    void getMetdataListTest() {
        String path = "/Users/khw/Downloads/image";

        try {

            //1. get fileDataList
            List<FileData> fileDataList = dataExtractorService.getImageFileList(path);
            Assertions.assertThat(fileDataList.size()).isGreaterThan(0);

            //2. get Metadata in FileData
            List<ImageMetadata> metadataList = fileDataList.stream()
                    .map(dataExtractorService::getImageMetadata)
                    .collect(Collectors.toList());

            //3. list size select
            Assertions.assertThat(metadataList.size()).isGreaterThan(0);

            metadataList.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
