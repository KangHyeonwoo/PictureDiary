package com.picture.diary.picture.file.data;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 사진 객체
 */
@Getter
@ToString
@Slf4j
public class Picture {

    //파일 명, 확장자를 제외한 파일 경로
    private String path;

    //확장자 및 경로를 제외한 파일 명
    private String name;

    //파일 확장자
    private Extensions extension;

    //파일 사이즈
    private long size;

    //좌표 (lat, lng)
    private Geometry geometry;

    //파일 생성 시간
    private LocalDateTime localDateTime;

    public Picture(File file) {
        String fullPath = file.getPath();
        String extStr = FilenameUtils.getExtension(fullPath);

        this.path = FilenameUtils.getFullPathNoEndSeparator(fullPath);
        this.name = FilenameUtils.getBaseName(file.getPath());
        this.extension = Extensions.valueOf(extStr.toUpperCase());
        this.size = file.length();

        this.geometry = this.findGeometry();
        this.localDateTime = this.findCreatedDateTime();
    }

    public String getFullPath() {
        // "\\" 요녀석 TODO Window 환경 Linux 환경 분기
        return this.path + "\\" + this.name + FilenameUtils.EXTENSION_SEPARATOR_STR + this.extension.name();
    }

    /*
        파일의 메타데이터 보유 유무는
        좌표 정보 혹은 파일 생성 일자 중 하나라도 존재하는지로 판단한다.
     */
    public boolean hasMetadata() {
        return this.geometry != null || this.localDateTime != null;
    }

    private Geometry findGeometry() {
        String filePath = getFullPath();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new File(filePath));

            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if(gpsDirectory != null) {
                double latitude = gpsDirectory.getGeoLocation().getLatitude();
                double longitude = gpsDirectory.getGeoLocation().getLongitude();

                return new Geometry(latitude, longitude);
            }
        } catch (NullPointerException | ImageProcessingException | IOException e) {
            log.error("[Geometry] File Not Read. File Path is [" + filePath + "].");
        } catch (Exception e) {
            log.error("[Geometry] Exception Occur.");
            e.printStackTrace();
        }

        return null;
    }

    private LocalDateTime findCreatedDateTime() {
        String filePath = getFullPath();

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new File(filePath));
            ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if(exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_DATETIME)) {
                String dateTimeStr = exifIFD0Directory.getDescription(ExifIFD0Directory.TAG_DATETIME);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

                return LocalDateTime.parse(dateTimeStr, formatter);
            }
        } catch (NullPointerException | ImageProcessingException | IOException e) {
            log.error("[DateTime] File Not Read. File Path is [" + filePath + "].");
        } catch (Exception e) {
            log.error("[DateTime] Exception Occur.");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Picture picture = (Picture) o;

        return name.equalsIgnoreCase(picture.name) &&
                path.equalsIgnoreCase(picture.path) &&
                size == picture.size &&
                geometry.equals(picture.geometry) &&
                localDateTime.equals(picture.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, size, geometry, localDateTime);
    }
}
