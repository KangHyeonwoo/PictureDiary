package com.picture.diary.extract.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.picture.diary.extract.data.*;
import com.picture.diary.extract.service.PictureExtractorService;
import com.picture.diary.utils.DateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureExtractorServiceImpl implements PictureExtractorService {

	private final PicturePathProperties picturePathProperties;
	
    public List<PictureFile> getPictureList(String path) throws IOException{
        Path folder = Paths.get(path);

        return Files.walk(folder)
            .map(data -> data.toFile())
            .filter(file -> file.getName().split(SplitParts.DOT.getValue()).length > 0)		//폴더 제외
            .filter(file -> {
                String fileName = file.getName();
                Extensions extension = Extensions.findOf(fileName);

                return extension != Extensions.NOT_ALLOWED;
            })
            .map(PictureFile::new)
            .collect(Collectors.toList());
    }

    public PictureMetadata getPictureMetadata(PictureFile pictureFile) {
    	PictureMetadata pictureMetadata = null;
    	
    	FileInputStream is = null;
    	Metadata metadata = null;
    	
        String fileName = pictureFile.getFileName();
        String path = pictureFile.getFilePath();

        try {
            is = new FileInputStream(path);
            metadata = ImageMetadataReader.readMetadata(is);

            Geometry geometry = this.getPictureGeometry(metadata);
            LocalDateTime pictureDate = this.getPictureDate(metadata);

            pictureMetadata = PictureMetadata.builder()
                    .fileName(fileName)
                    .geometry(geometry)
                    .pictureDate(pictureDate)
                    .build();

        } catch (ImageProcessingException ie) {
            log.error("Fail to load metadata. File name [{}]", fileName);
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
    
    public boolean movePictureToDataPath(PictureFile pictureFile) {
    	String dataPath = picturePathProperties.getDataPath(pictureFile);
    	boolean moveResult = this.movePictureFile(pictureFile, dataPath);
    	
    	if(moveResult) {
    		pictureFile.changeFilePath(dataPath);
    	}
    	
    	return moveResult;
    }
    
    public boolean movePictureToTempPath(PictureFile pictureFile) {
    	String tempPath = picturePathProperties.getTempPath(pictureFile);
    	boolean moveResult = this.movePictureFile(pictureFile, tempPath);
    	
    	if(moveResult) {
    		pictureFile.changeFilePath(tempPath);
    	}
    	
    	return moveResult;
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
    
    private boolean movePictureFile(PictureFile pictureFile, String to) {
    	String from = pictureFile.getFilePath();
    	
    	Path fromPath = Paths.get(from);
    	Path toPath = Paths.get(to);
    	CopyOption[] copyOptions = {};
    	
    	try {
			Files.move(fromPath, toPath, copyOptions);
			return true;
			
		} catch (IOException e) {
			log.error("Fail the move file. Move path [{} -> {}]", from, to);
			return false;
		}
    }
}