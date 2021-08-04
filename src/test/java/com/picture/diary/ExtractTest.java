package com.picture.diary;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.picture.diary.extract.data.Geometry;
import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.extract.service.PictureExtractService;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.service.PictureService;

@SpringBootTest
public class ExtractTest {

    @Autowired
    PictureExtractService pictureExtractorService;

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
            assertThat(fileDataList.size()).isGreaterThan(0);

            //2. get Metadata in FileData
            List<PictureMetadata> metadataList = fileDataList.stream()
                    .map(fileData -> {
                    	String fromPath = picturePathProperties.getFromPath(fileData.getFileName(), fileData.getExtension());
                    	return pictureExtractorService.getPictureMetadata(fromPath);
                    })
                    .collect(Collectors.toList());

            //3. list size select
            assertThat(metadataList.size()).isGreaterThan(0);
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
    	
    	assertThat(beforeSize).isEqualTo(dataSize);
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
    	String path = "C:/Users/KHW-IPC/Pictures/test/IMG_999.JPG";
    	File inputFile = new File(path);
    	String outputFilePath = "C:/Users/KHW-IPC/Pictures/test/IMG_999_2.JPG";
    	try(FileOutputStream fos = new FileOutputStream(outputFilePath);
    			OutputStream os = new BufferedOutputStream(fos)) {
    		
    		TiffOutputSet outputSet = null;
    		
    		final ImageMetadata metadata = Imaging.getMetadata(inputFile);
    		final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
    		if (null != jpegMetadata) {
                final TiffImageMetadata exif = jpegMetadata.getExif();

                if (null != exif) {
                    outputSet = exif.getOutputSet();
                }
    		}
    		if (null == outputSet) {
                outputSet = new TiffOutputSet();
            }
    		
            final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
            exifDirectory.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
            exifDirectory.add(ExifTagConstants.EXIF_TAG_APERTURE_VALUE, new RationalNumber(3, 10));
            
            final double latitude = 37;
            final double longitude = 126;

            outputSet.setGPSInDegrees(longitude, latitude);
            
    		new ExifRewriter().updateExifMetadataLossless(inputFile, os, outputSet);
    		
    		Files.delete(Paths.get(path));
    		Files.move(Paths.get(outputFilePath), Paths.get(path));
    	} catch (ImageReadException e) {
    		e.printStackTrace();
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImageWriteException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    @DisplayName("좌표 추가")
    public void setGeometry() {
    	String path = "C:/Users/KHW-IPC/Pictures/test/no_geometry.JPG";
    	String outputPath = "C:/Users/KHW-IPC/Pictures/test/no_geometry22.JPG";
    	File jpegImageFile = new File(path);
    	
    	try (FileOutputStream fos = new FileOutputStream(new File(outputPath));
                OutputStream os = new BufferedOutputStream(fos)) {
            TiffOutputSet outputSet = null;

            final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            
            if (null != jpegMetadata) {
                final TiffImageMetadata exif = jpegMetadata.getExif();

                if (null != exif) {
                    outputSet = exif.getOutputSet();
                }
            }

            if (null == outputSet) {
                outputSet = new TiffOutputSet();
            }

            final double longitude = -74.0;
            final double latitude = 40 + 43 / 60.0;

            outputSet.setGPSInDegrees(longitude, latitude);

            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os, outputSet);
            
            Files.delete(Paths.get(path));
    		Files.move(Paths.get(outputPath), Paths.get(path));
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImageReadException e) {
			e.printStackTrace();
		} catch (ImageWriteException e) {
			e.printStackTrace();
		}
    }

    @Test
    @DisplayName("좌표 조회")
    public void selectGeometry() {
    	//String path = "C:/Users/KHW-IPC/Pictures/test/no_geometry.JPG";
    	String path = "C:/Users/KHW-IPC/Pictures/test/SEGMENT_ISSUE_IMG_1754_RESULT.JPG";
    	File file = new File(path);
    	try {
			final ImageMetadata metadata = Imaging.getMetadata(file);
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			
			double latitude = jpegMetadata.getExif().getGPS().getLatitudeAsDegreesNorth();
			double longitude = jpegMetadata.getExif().getGPS().getLongitudeAsDegreesEast();
			
    	} catch (NullPointerException e){
    		//좌표 없는 경우
    		System.out.println("no geometry");
		} catch (ImageReadException | IOException e) {
			
			e.printStackTrace();
		}
    }
    
    @Test
    @DisplayName("날짜 조회 - 날짜 있는 경우")
    public void selectDate() {
    	String path = "C:/Users/KHW-IPC/Pictures/test/SEGMENT_ISSUE_IMG_1754_RESULT.JPG";
    	File file = new File(path);
    	try {
			final ImageMetadata metadata = Imaging.getMetadata(file);
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			
			String[] tagInfos = jpegMetadata.getExif().getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
			for(int i=0; i<tagInfos.length; i++) {
				System.out.println(tagInfos[i]);
			}
    	} catch (NullPointerException e){
    		System.out.println("no date");
		} catch (ImageReadException | IOException e) {
			
			e.printStackTrace();
		}
    }
    
    @Test
    @DisplayName("날짜 형식에 안맞는 오류 => 오류 안나고 공백 리턴됨")
    public void dateException() {
    	String pictureDate = "2021:07:13";
		//Convert
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
        
        Assertions.assertThrows(Exception.class, () -> LocalDateTime.parse(pictureDate, inputFormat));
    }
    
    @Test
    @DisplayName("파일 조회해서 좌표 변경하기(좌표정보 O)")
    void readImageFileAndSetGeometry() {
    	//10750
    	long fileId = 11;
    	
    	PictureDto pictureDto = pictureService.findByPictureId(fileId);
    	//좌표정보 있어야 함
    	assertThat(pictureDto.isHasGeometry()).isEqualTo(true);
    	
    	double latitude = 37.1;
    	double longitude = 126.1;
    	
    	pictureService.updateGeometry(fileId, latitude, longitude);
    	
    	PictureDto afterPictureDto = pictureService.findByPictureId(fileId);
    	
    	assertThat(afterPictureDto.getLatitude()).isEqualTo(latitude);
    	assertThat(afterPictureDto.getLongitude()).isEqualTo(longitude);
    }
    
    @Test
    @DisplayName("파일 조회해서 좌표 변경하기(좌표정보 X)")
    void readHasNotGeometryFileAndSetGeometry() {
    	
    	//10750
    	long fileId = 44;
    	
    	PictureDto pictureDto = pictureService.findByPictureId(fileId);
    	//좌표정보 없어야 함
    	assertThat(pictureDto.isHasGeometry()).isEqualTo(false);
    	
    	double latitude = 37.7;
    	double longitude = 126.7;
    	
    	pictureService.updateGeometry(fileId, latitude, longitude);
    	
    	PictureDto afterPictureDto = pictureService.findByPictureId(fileId);
    	
    	//좌표정보 있어야 함.
    	assertThat(afterPictureDto.isHasGeometry()).isEqualTo(true);
    }
    
}
