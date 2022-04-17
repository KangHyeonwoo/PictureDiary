package com.picture.diary.extract;

import com.picture.diary.extract.data.ExtractPath;
import com.picture.diary.extract.data.Geometry;
import com.picture.diary.extract.data.Picture;
import lombok.RequiredArgsConstructor;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NasPictureExtractor extends AbstractPictureExtractor {

    private final ExtractPath extractPath;

    @Override
    protected List<Picture> findPictureListByUserId(String userId) {
        List<Picture> pictureList = new ArrayList<>();
        String path = extractPath.getTargetFolderPath() + "/" + userId;

        return pictureList;
    }

    private Geometry getGeometry(File file) {

        try {
            final ImageMetadata metadata = Imaging.getMetadata(file);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

            double latitude = jpegMetadata.getExif().getGPS().getLatitudeAsDegreesNorth();
            double longitude = jpegMetadata.getExif().getGPS().getLongitudeAsDegreesEast();

            return new Geometry(latitude, longitude);
        } catch (ImageReadException | NullPointerException | IOException e) {
            return null;
        }
    }

    private LocalDateTime getDate(File file) {
        try {
            final ImageMetadata metadata = Imaging.getMetadata(file);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

            String pictureDate = jpegMetadata.getExif().getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL)[0];
            //Convert
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

            return LocalDateTime.parse(pictureDate, inputFormat);
        } catch (ImageReadException | NullPointerException | IOException e) {
            return null;
        }
    }
}
