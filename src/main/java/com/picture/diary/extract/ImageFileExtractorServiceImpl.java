package com.picture.diary.extract;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.picture.diary.extract.data.*;
import com.picture.diary.extract.exception.NotFoundDateException;
import com.picture.diary.extract.exception.NotFoundGeometryException;
import com.picture.diary.utils.DateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
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
                    .geometry(geometry)
                    .imageDate(imageDate)
                    .build();

        } catch (NotFoundGeometryException e) {
            log.error("Not Found GeoLocation. File Name [{}]", fileName);
            LocalDateTime imageDate = this.getImageDate(metadata);
            
            return ImageMetadata.builder()
                    .fileName(fileName)
                    .imageDate(imageDate)
                    .build();
            
        } catch (NotFoundDateException e) {
        	log.error("Not Found Date. File Name [{}]", fileName);
        	
        	return ImageMetadata.builder()
                    .fileName(fileName)
                    .build();
        	
        } catch (ImageProcessingException ie) {
            log.error("Fail to load metadata. File name [{}]", fileName);
            return null;
            
        } catch (IOException ie) {
            log.error("IOException occur.");
            return null;
        }
    }
    
    private Geometry getImageGeometry(Metadata metadata) throws NotFoundGeometryException {
    	try {
    		GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            GeoLocation location = gpsDirectory.getGeoLocation();

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            return new Geometry(latitude, longitude);
    	} catch (NullPointerException e) {
    		throw new NotFoundGeometryException();
    	}
    }

    private LocalDateTime getImageDate(Metadata metadata) throws NotFoundDateException{
    	try {
    		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            Date imageDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

            return DateUtils.convertToLocalDateTimeViaInstant(imageDate);
    	} catch (NullPointerException e) {
    		throw new NotFoundDateException();
    	}
    }
}
