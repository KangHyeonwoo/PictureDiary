package com.picture.diary.extract;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.picture.diary.extract.data.*;
import com.picture.diary.utils.DateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageFileExtractorServiceImpl {

	private final FilePathProperties filePathProperties;
	
    public List<ImageFile> getImageFileList(String path) throws IOException{
        Path folder = Paths.get(path);

        return Files.walk(folder)
            .map(data -> data.toFile())
            .filter(file -> file.getName().split(SplitParts.DOT.getValue()).length > 0)
            .filter(file -> {
                String fileName = file.getName();
                Extensions extension = Extensions.findOf(fileName);

                return extension != Extensions.NOT_ALLOWED;
            })
            .map(ImageFile::new)
            .collect(Collectors.toList());
    }

    public ImageMetadata getImageMetadata(ImageFile imageFile) {
    	FileInputStream is = null;
    	Metadata metadata = null;
    	
        String fileName = imageFile.getFileName();
        String path = imageFile.getFilePath();

        try {
            is = new FileInputStream(path);
            metadata = ImageMetadataReader.readMetadata(is);

            Geometry geometry = this.getImageGeometry(metadata);
            LocalDateTime imageDate = this.getImageDate(metadata);

            return ImageMetadata.builder()
                    .fileName(fileName)
                    .geometry(geometry)
                    .imageDate(imageDate)
                    .build();

        } catch (ImageProcessingException ie) {
            log.error("Fail to load metadata. File name [{}]", fileName);
            return null;
            
        } catch (IOException ie) {
            log.error("IOException occur.");
            return null;
        }
    }
    
    private Geometry getImageGeometry(Metadata metadata) {
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

    private LocalDateTime getImageDate(Metadata metadata) {
    	try {
    		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            Date imageDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

            return DateUtils.convertToLocalDateTimeViaInstant(imageDate);
    	} catch (NullPointerException e) {

    		return null;
    	}
    }
    
    public boolean moveImageFileToDataPath(ImageFile imageFile) {
    	String dataPath = filePathProperties.getDataPath();
    	boolean moveResult = this.moveImageFile(imageFile, dataPath);
    	
    	if(moveResult) {
    		imageFile.changeFilePath(dataPath);
    	}
    	
    	return moveResult;
    }
    
    public boolean moveImageFileToTempPath(ImageFile imageFile) {
    	String tempPath = filePathProperties.getTempPath();
    	boolean moveResult = this.moveImageFile(imageFile, tempPath);
    	
    	if(moveResult) {
    		imageFile.changeFilePath(tempPath);
    	}
    	
    	return moveResult;
    }
    
    private boolean moveImageFile(ImageFile imageFile, String to) {
    	String from = StringUtils.hasLength(imageFile.getFilePath()) ? filePathProperties.getFromPath() : imageFile.getFilePath();
    	
    	Path fromPath = Paths.get(from);
    	Path toPath = Paths.get(to);
    	CopyOption[] copyOptions = {StandardCopyOption.COPY_ATTRIBUTES};
    	
    	try {
			Files.move(fromPath, toPath, copyOptions);
			return true;
			
			
		} catch (IOException e) {
			//TODO 에러 유형 확인 후 삭제 필요함.
			e.printStackTrace();
			
			log.error("Fail the move file. Move path [{} -> {}]", from, to);
			return false;
		}
    }
}
