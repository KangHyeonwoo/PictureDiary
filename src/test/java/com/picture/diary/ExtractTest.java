package com.picture.diary;

import com.picture.diary.extract.ImageFileExtractorServiceImpl;
import com.picture.diary.extract.data.FilePathProperties;
import com.picture.diary.extract.data.ImageFile;
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

    @Autowired
    FilePathProperties filePathProperties;
    
    
    @Test
    void getMetdataListTest() {
        String path = filePathProperties.getFromPath();

        try {

            //1. get fileDataList
            List<ImageFile> fileDataList = dataExtractorService.getImageFileList(path);
            Assertions.assertThat(fileDataList.size()).isGreaterThan(0);

            //2. get Metadata in FileData
            List<ImageMetadata> metadataList = fileDataList.stream()
                    .map(dataExtractorService::getImageMetadata)
                    .collect(Collectors.toList());

            //3. list size select
            Assertions.assertThat(metadataList.size()).isGreaterThan(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    void moveFileTest() throws Exception {
    	String path = filePathProperties.getFromPath();
    	
    	List<ImageFile> fileDataList = dataExtractorService.getImageFileList(path);
    	int beforeSize = fileDataList.size();
    	
    	fileDataList.stream()
    	.map(fileData -> {
			ImageMetadata imageMetadata = dataExtractorService.getImageMetadata(fileData);
			fileData.addMetadata(imageMetadata);
			
			return fileData;
		})
    	.forEach(fileData -> {
    		if(fileData.getImageMetadata().getGeometry() == null) {
    			dataExtractorService.moveImageFileToTempPath(fileData);
    		} else {
    			dataExtractorService.moveImageFileToDataPath(fileData);
    		}
    	});
    	
    	
    	String dataPath = filePathProperties.getDataPath();
    	String tempPath = filePathProperties.getTempPath();
    	int dataSize = dataExtractorService.getImageFileList(dataPath).size();
    	int tempSize = dataExtractorService.getImageFileList(tempPath).size();
    	
    	Assertions.assertThat(beforeSize).isEqualTo(dataSize + tempSize);
    }


}
