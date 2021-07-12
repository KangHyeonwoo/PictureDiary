package com.picture.diary;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    
    
    //@Test
    void getMetadataListTest() {
        String path = picturePathProperties.getFromPath();

        try {

            //1. get fileDataList
            List<PictureFile> fileDataList = pictureExtractorService.getPictureList(path);
            Assertions.assertThat(fileDataList.size()).isGreaterThan(0);

            //2. get Metadata in FileData
            List<PictureMetadata> metadataList = fileDataList.stream()
                    .map(fileData -> {
                    	String fromPath = picturePathProperties.getFromPath(fileData.getFileName(), fileData.getExtension());
                    	return pictureExtractorService.getPictureMetadata(fromPath);
                    })
                    .collect(Collectors.toList());

            //3. list size select
            Assertions.assertThat(metadataList.size()).isGreaterThan(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //@Test
    void moveFileTest() throws Exception {
    	String path = picturePathProperties.getFromPath();
    	
    	List<PictureFile> fileDataList = pictureExtractorService.getPictureList(path);
    	int beforeSize = fileDataList.size();
    	
    	fileDataList.stream()
    	.map(fileData -> {
    		String fromPath = picturePathProperties.getFromPath(fileData.getFileName(), fileData.getExtension());
			PictureMetadata pictureMetadata = pictureExtractorService.getPictureMetadata(fromPath);
			fileData.addMetadata(pictureMetadata);
			
			return fileData;
		})
    	.forEach(fileData -> {
    		//pictureExtractorService.movePictureFile(fileData);
    	});
    	
    	
    	String dataPath = picturePathProperties.getDataPath();
    	int dataSize = pictureExtractorService.getPictureList(dataPath).size();
    	
    	Assertions.assertThat(beforeSize).isEqualTo(dataSize);
    }

    //@Test
    void extractTest() {
    	pictureService.pictureExtract();
    }
    
    //https://github.com/apache/commons-imaging/blob/master/src/test/java/org/apache/commons/imaging/examples/WriteExifMetadataExample.java
    //https://stackoverflow.com/questions/40241030/write-exif-data-to-tiff-file-using-apache-commons-imaging
    //https://stackoverflow.com/questions/36868013/editing-jpeg-exif-data-with-java
    @Test
    @DisplayName("GPS 좌표가 있는 경우 GPS 갱신 -> JPEG만 먼저 테스트 Extensions enum에서 처리해야함, HEIC도 테스트 필요함")
    void updateGeometry() {
//    	String path = "C:/Users/KHW-IPC/Pictures/test/geometry_test2.jpeg";
    	String path = "C:/Users/KHW-IPC/Pictures/test/IMG_1889.JPG";
    	File file = new File(path);
    	try {
			final ImageMetadata metadata = Imaging.getMetadata(file);
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			
			if(null != jpegMetadata) {
				System.out.println(jpegMetadata.getExif().getGPS().getLatitudeAsDegreesNorth());
				System.out.println(jpegMetadata.getExif().getGPS().getLongitudeAsDegreesEast());
			}
		} catch (ImageReadException | IOException e) {
			e.printStackTrace();
		}
    	
    }
}
