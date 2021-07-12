package com.picture.diary.extract.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.picture.diary.extract.data.Extensions;
import com.picture.diary.extract.data.Geometry;
import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.extract.data.SplitParts;
import com.picture.diary.extract.service.PictureExtractorService;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.utils.DateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureExtractorServiceImpl implements PictureExtractorService {

	private final PicturePathProperties picturePathProperties;
	
    public List<PictureFile> getPictureList(String path) {
        Path folder = Paths.get(path);
        List<PictureFile> pictureFileList = new ArrayList<>();

		try {
			pictureFileList = Files.walk(folder)
					.map(data -> data.toFile())
					.filter(file -> file.getName().split(SplitParts.DOT.getValue()).length > 0)		//폴더 제외
					.filter(file -> {
						String fileName = file.getName();
						Extensions extension = Extensions.findOf(fileName);

						return extension != Extensions.NOT_ALLOWED;
					})
					.map(PictureFile::new)
					.collect(Collectors.toList());

		} catch(IOException e) {
			log.error("can not find path [{}]", path);
		}

        return pictureFileList;
    }

    public PictureMetadata getPictureMetadata(String path) {
    	PictureMetadata pictureMetadata = new PictureMetadata();

    	FileInputStream is = null;
    	Metadata metadata = null;

        try {
            is = new FileInputStream(path);
            metadata = ImageMetadataReader.readMetadata(is);

            Geometry geometry = this.getPictureGeometry(metadata);
            LocalDateTime pictureDate = this.getPictureDate(metadata);

            pictureMetadata = PictureMetadata.builder()
            		.metadata(metadata)
                    .geometry(geometry)
                    .pictureDate(pictureDate)
                    .build();

        } catch (ImageProcessingException ie) {
            log.error("Fail to load metadata. File path [{}]", path);
        } catch (IOException ie) {
            log.error("IOException occur.");
        } finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        
        return pictureMetadata;
    }
    
    public boolean doubleCheck(PictureFile pictureFile, List<PictureDto> savedPictureList) {
    	
    	return savedPictureList.stream()
    			.anyMatch(savedPicture -> 
    				pictureFile.getFileName().equals(savedPicture.getPictureOriginName()) && 
    				savedPicture.getPictureSize() == pictureFile.getFileSize()
    	);
    }
    
    public boolean movePictureFile(String fromFilePath, String toFilePath) {
    	
    	Path fromPath = Paths.get(fromFilePath);
    	Path toPath = Paths.get(toFilePath);
    	CopyOption[] copyOptions = {};
    	
    	try {
			Files.move(fromPath, toPath, copyOptions);
			return true;
			
		} catch (IOException e) {
			log.error("Fail the move file. Move path [{} -> {}]", fromFilePath, toFilePath);
			return false;
		}
    }
    
    public String getExtractFolderPath() {
    	
    	return picturePathProperties.getFromPath();
    }
    
    public boolean setPictureGeometry(String path, Geometry geometry) {
    	FileInputStream is = null;
    	Metadata metadata = null;
    	boolean isSuccess = false;
    	
    	try {
    		is = new FileInputStream(path);
            metadata = ImageMetadataReader.readMetadata(is);
	    	GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
	    	
	    	//gpsDirectory.setDouble(GpsDirectory.TAG_LATITUDE, geometry.getLatitude());
	    	//gpsDirectory.setDouble(GpsDirectory.TAG_LONGITUDE, geometry.getLongitude());
	    	
	    	GeoLocation geoLocation = gpsDirectory.getGeoLocation();
	    	System.out.println("****************");
	    	System.out.println(geoLocation.getLatitude() + ", " + geoLocation.getLongitude());
	    	
	    	isSuccess = true;
    	} catch (Exception e) {
    		e.printStackTrace();
    		log.error(e.toString());
    		log.error("Fail to set geometry. File Path [{}]", path);
    	}
    	
    	return isSuccess;
    }
    
    private Geometry getPictureGeometry(Metadata metadata) {
    	try {
    		GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            GeoLocation location = gpsDirectory.getGeoLocation();
            
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            return new Geometry(latitude, longitude);
    	} catch (NullPointerException e) {
    		return null;
    	}
    }

    private LocalDateTime getPictureDate(Metadata metadata) {
    	try {
    		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            Date pictureDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            
            return DateUtils.convertToLocalDateTimeViaInstant(pictureDate);
    	} catch (NullPointerException e) {
    		return null;
    	}
    }

}
