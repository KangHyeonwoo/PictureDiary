package com.picture.diary;

import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.extract.service.PictureExtractorService;
import com.picture.diary.extract.service.impl.PictureExtractorServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class ExtractTest {

    @Autowired
    PictureExtractorService pictureExtractorService;

    @Autowired
    PicturePathProperties picturePathProperties;
    
    
    @Test
    void getMetdataListTest() {
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
    		if(fileData.getPictureMetadata().getGeometry() == null) {
    			pictureExtractorService.movePictureToTempPath(fileData);
    		} else {
    			pictureExtractorService.movePictureToDataPath(fileData);
    		}
    	});
    	
    	
    	String dataPath = picturePathProperties.getDataPath();
    	String tempPath = picturePathProperties.getTempPath();
    	int dataSize = pictureExtractorService.getPictureList(dataPath).size();
    	int tempSize = pictureExtractorService.getPictureList(tempPath).size();
    	
    	Assertions.assertThat(beforeSize).isEqualTo(dataSize + tempSize);
    }


}
