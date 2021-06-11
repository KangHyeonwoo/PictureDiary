package com.picture.diary.extract;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.picture.diary.extract.data.Extensions;
import com.picture.diary.extract.data.FileData;
import com.picture.diary.extract.data.ImageMetadata;
import com.picture.diary.extract.data.SplitParts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataExtractorServiceImpl {

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

    //TODO : 소스코드 너무 지저분함 !! 정리하기
    public ImageMetadata getImageMetadata(FileData fileData) {
        String fileName = fileData.getFileName();
        String path = fileData.getFilePath();

        try {
            FileInputStream is = new FileInputStream(path);
            Metadata metadata = ImageMetadataReader.readMetadata(is);

            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            GeoLocation location = gpsDirectory.getGeoLocation();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            return ImageMetadata.builder()
                    .fileName(fileName)
                    .latitude(latitude)
                    .longitude(longitude)
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
}
