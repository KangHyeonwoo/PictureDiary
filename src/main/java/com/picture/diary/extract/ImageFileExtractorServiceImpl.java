package com.picture.diary.extract;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.picture.diary.extract.data.*;
import com.picture.diary.utils.DateUtils;
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
public class ImageFileExtractorServiceImpl {

    public List<FileData> getImageFileList(String path) throws IOException{
        Path folder = Paths.get(path);

        return Files.walk(folder)
            .map(data -> data.toFile())
            .filter(file -> file.getName().split(SplitParts.DOT.getValue()).length > 0)
            .filter(file -> {
                String fileName = file.getName();
                String extension = Extensions.findOf(fileName).toString();

                return Extensions.contains(extension);
            })
            .map(FileData::new)
            .collect(Collectors.toList());
    }

    public ImageMetadata getImageMetadata(FileData fileData) {
        String fileName = fileData.getFileName();
        String path = fileData.getFilePath();

        try {
            FileInputStream is = new FileInputStream(path);
            Metadata metadata = ImageMetadataReader.readMetadata(is);

            Geometry geometry = this.getImageGeometry(metadata);
            LocalDateTime dateTime = this.getImageDate(metadata);

            return ImageMetadata.builder()
                    .latitude(geometry.getLatitude())
                    .longitude(geometry.getLongitude())
                    .date(dateTime)
                    .build();

        } catch (NullPointerException e) {
            log.error("Not Found GeoLocation. File Name [{}]", fileName);
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

    private Geometry getImageGeometry(Metadata metadata) {

        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        GeoLocation location = gpsDirectory.getGeoLocation();

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        return new Geometry(latitude, longitude);
    }

    private LocalDateTime getImageDate(Metadata metadata) {
        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Date imageDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

        return DateUtils.convertToLocalDateTimeViaInstant(imageDate);
    }
}
