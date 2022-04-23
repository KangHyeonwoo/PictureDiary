package com.picture.diary;

import com.groupdocs.metadata.Metadata;
import com.groupdocs.metadata.core.IDocumentInfo;
import com.groupdocs.metadata.core.IExif;
import com.groupdocs.metadata.core.TiffTag;
import com.groupdocs.metadata.internal.c.a.w.internal.Ex;
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

    @Test
    @DisplayName("디렉토리 내 파일 목록 조회")
    void getFileList() throws Exception {
        String filePath = "D:/test/IMG_2068_copy.HEIC";
        Path path = Paths.get(filePath);

        BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
        System.out.println("hi");
        System.out.println(basicFileAttributes);

        UserDefinedFileAttributeView userDefinedFileAttributeView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        System.out.println("bye");
        System.out.println(userDefinedFileAttributeView);

    }

}
