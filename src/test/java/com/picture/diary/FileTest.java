package com.picture.diary;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import com.drew.metadata.heif.HeifDirectory;
import com.picture.diary.extract.util.PictureExtractUtils;
import com.picture.diary.picture.file.data.Geometry;
import com.picture.diary.picture.file.data.Picture;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileTest {

    @Autowired
    PictureExtractUtils pictureExtractUtils;

    @Test
    @DisplayName("FilenameUtils 클래스의 메소드들의 사용법 확인")
    void filenameUtilsTest() {
        String from = "D:/p/aaaa.txt";

        String basename = FilenameUtils.getBaseName(from);
        String extension = FilenameUtils.getExtension(from);
        String fullPathNoEndSeparator = FilenameUtils.getFullPathNoEndSeparator(from);
        String path = FilenameUtils.getPath(from);

        Assertions.assertThat(basename).isEqualTo("aaaa");
        Assertions.assertThat(extension).isEqualTo("txt");
        Assertions.assertThat(fullPathNoEndSeparator).isEqualTo("D:/p");
        Assertions.assertThat(path).isEqualTo("p/");
    }

    @Test
    @DisplayName("파일 복사 테스트")
    void copyTest() throws Exception{
        String fromPath = "Z:/home/Photos/MobileBackup/iPhone (2)/2021/02/IMG_2068.HEIC";
        String toPath = "D:/IMG_2068_copy.HEIC";
        File fromFile = new File(fromPath);
        File toFile = new File(toPath);

        //Path resultPath = Files.copy(fromFile.toPath(), toFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);

    }
/*
    //HEIC 확장자 안됨. 다른 라이브러리 찾는중
    @Test
    @DisplayName("HEIC 확장자 메타데이터 조회. ")
    void getMetadataOfHEICPicture() throws Exception {
        String filePath = "D:/IMG_2068_copy.HEIC";
        Metadata metadata = new Metadata(filePath);
        IExif root = (IExif) metadata.getRootPackage();

        if (root.getExifPackage() != null) {
            // Get GPS information
            System.out.println("GPS Information");
            System.out.println(root.getExifPackage().getGpsPackage().getLatitudeRef());
            System.out.println(root.getExifPackage().getGpsPackage().getLongitude());
            System.out.println("GPS Information size : " + root.getExifPackage().getGpsPackage().toList().getCount());

        }
    }
*/
    @Test
    @DisplayName("HEIC 확장자 메타데이터 조회")
    void getMetadataOfHeicPicture() throws Exception {
        String filePath = "D:/test/IMG_2068_copy.HEIC";
        Metadata metadata = ImageMetadataReader.readMetadata(new File(filePath));

        metadata.getDirectories().forEach(System.out::println);

        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        if(gpsDirectory != null) {
            System.out.println(gpsDirectory.getGeoLocation().getLatitude());
            System.out.println(gpsDirectory.getGeoLocation().getLongitude());
        }

        metadata.getDirectories().forEach(directory -> {
            System.out.println("Directory Name : " + directory.getName());
            directory.getTags().forEach(tag -> {
                System.out.println("  Tag Name : " + tag.getTagName());
                System.out.println("  Tag Description : " + tag.getDescription());
            });
        });
    }

}
