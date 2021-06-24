package com.picture.diary;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.extract.service.PictureExtractorService;
import com.picture.diary.picture.service.PictureService;

@SpringBootTest
public class ExtractTest {

    @Autowired
    PictureExtractorService pictureExtractorService;

    @Autowired
    PicturePathProperties picturePathProperties;
    
    @Autowired
    PictureService pictureService;
    
    
    @Test
    void getMetadataListTest() {
        String path = picturePathProperties.getFromPath();

        try {

            //1. get fileDataList
            List<PictureFile> fileDataList = pictureExtractorService.getPictureList(path);
            Assertions.assertThat(fileDataList.size()).isGreaterThan(0);

            //2. get Metadata in FileData
            List<PictureMetadata> metadataList = fileDataList.stream()
                    .map(pictureExtractorService::getPictureMetadata)
                    .collect(Collectors.toList());

            //3. list size select
            Assertions.assertThat(metadataList.size()).isGreaterThan(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    void moveFileTest() throws Exception {
    	String path = picturePathProperties.getFromPath();
    	
    	List<PictureFile> fileDataList = pictureExtractorService.getPictureList(path);
    	int beforeSize = fileDataList.size();
    	
    	fileDataList.stream()
    	.map(fileData -> {
			PictureMetadata pictureMetadata = pictureExtractorService.getPictureMetadata(fileData);
			fileData.addMetadata(pictureMetadata);
			
			return fileData;
		})
    	.forEach(fileData -> {
    		pictureExtractorService.movePictureFile(fileData);
    	});
    	
    	
    	String dataPath = picturePathProperties.getDataPath();
    	int dataSize = pictureExtractorService.getPictureList(dataPath).size();
    	
    	Assertions.assertThat(beforeSize).isEqualTo(dataSize);
    }

    @Test
    void extractTest() {
    	pictureService.pictureExtract();
    }
}
