package com.picture.diary;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileTest {

    @Test
    @DisplayName("HEIC 확장자 GPS, DATE 메타데이터 조회")
    void getGpsDateMetadataOfHeicPicture() throws Exception {
        //set file info
        final String fileName = "IMG_2074.HEIC";
        final double latitude = 34.80521666666667;
        final double longitude = 126.43221944444446;
        final String dateTime = "2021:02:12 22:10:22";

        this.compareMetadata(fileName, latitude, longitude, dateTime);
    }

    @Test
    @DisplayName("JPG 확장자 GPS, DATE 메타데이터 조회")
    void getGpsDateMetadataOfJpgPicture()  throws Exception{
        //set file info
        final String fileName = "IMG_1822.JPG";
        final double latitude = 34.794888888888885;
        final double longitude = 126.3737256;
        final String dateTime = "2017:01:29 19:40:09";

        this.compareMetadata(fileName, latitude, longitude, dateTime);
    }

    @Test
    @DisplayName("JPEG 확장자 GPS, DATE 메타데이터 조회")
    void getGpsDateMetadataOfJpegPicture()  throws Exception{
        final String fileName = "11546.JPEG";
        final double latitude = 37.49995;
        final double longitude = 127.0284361111111;
        final String dateTime = "2020:07:18 15:36:45";

        this.compareMetadata(fileName, latitude, longitude, dateTime);
    }

    private void compareMetadata(String fileName, double latitude, double longitude, String dateTime) throws Exception {
        //read file metadata
        Metadata metadata = ImageMetadataReader.readMetadata(this.getTestFileByFileName(fileName));
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

        //assert geometry
        Assertions.assertThat(gpsDirectory.getGeoLocation().getLatitude()).isEqualTo(latitude);
        Assertions.assertThat(gpsDirectory.getGeoLocation().getLongitude()).isEqualTo(longitude);

        //assert date
        String compareDateTime = exifIFD0Directory.getDescription(ExifIFD0Directory.TAG_DATETIME);
        Assertions.assertThat(compareDateTime).isEqualTo(dateTime);
    }

    private File getTestFileByFileName(String fileName) {

        return Paths.get("src", "test","resources","images", fileName).toFile();
    }

}
