package com.picture.diary.extract.service.impl;

import static com.picture.diary.extract.exception.PictureExtractExceptionType.ETC_EXCEPTION;
import static com.picture.diary.extract.exception.PictureExtractExceptionType.FILE_NOT_FOUND;
import static com.picture.diary.extract.exception.PictureExtractExceptionType.METADATA_READ_FAILED;
import static com.picture.diary.extract.exception.PictureExtractExceptionType.METADATA_WRITE_FAILED;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffImageWriterLossless;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.extract.data.Extensions;
import com.picture.diary.extract.data.Geometry;
import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.extract.data.SplitParts;
import com.picture.diary.extract.service.PictureExtractorService;
import com.picture.diary.extract.util.PictureExtractUtils;
import com.picture.diary.picture.data.PictureDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureExtractorServiceImpl implements PictureExtractorService {

	private final PicturePathProperties picturePathProperties;
	private final PictureExtractUtils pictureExtractUtils;
	
	public List<PictureFile> getPictureList(String path) {
		Path folder = Paths.get(path);
		List<PictureFile> pictureFileList = new ArrayList<>();

		try {
			pictureFileList = Files.walk(folder)
					.map(data -> data.toFile())
					.filter(file -> file.getName().split(SplitParts.DOT.getValue()).length > 0)		//폴더 제외
					.filter(file -> {
						String fileName = file.getName();
						Extensions extension = Extensions.findOf(fileName);

						return extension != Extensions.NOT_ALLOWED;
					})
					.map(PictureFile::new)
					.collect(Collectors.toList());

		} catch(IOException e) {
			log.error("can not find path [{}]", path);
		}

		return pictureFileList;
    }

	public PictureMetadata getPictureMetadata(String path) {
		File file = new File(path);

		try {
			PictureMetadata pictureMetadata = new PictureMetadata();
			
			pictureExtractUtils.removeDuplicatedApp13SegMents(path);
        	
			final ImageMetadata metadata = Imaging.getMetadata(file);
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        	
			Geometry geometry = this.getPictureGeometry(jpegMetadata);
			LocalDateTime pictureDate = this.getPictureDate(jpegMetadata);

			pictureMetadata = PictureMetadata.builder()
				.geometry(geometry)
				.pictureDate(pictureDate)
				.build();
			return pictureMetadata;
		} catch (ImageReadException ie) {
			log.error("Fail to load metadata. File path [{}]", path);
			return null;
		} catch (IOException ie) {
			log.error("IOException occur.");
			return null;
		}
	}

	public boolean doubleCheck(PictureFile pictureFile, List<PictureDto> savedPictureList) {
    	
    	return savedPictureList.stream()
    			.anyMatch(savedPicture -> 
    				pictureFile.getFileName().equals(savedPicture.getPictureOriginName()) && 
    				savedPicture.getPictureSize() == pictureFile.getFileSize()
    	);
    }
    
    public boolean movePictureFile(String fromFilePath, String toFilePath) {
    	
    	Path fromPath = Paths.get(fromFilePath);
    	Path toPath = Paths.get(toFilePath);
    	CopyOption[] copyOptions = {};
    	
    	try {
    		Files.move(fromPath, toPath, copyOptions);
    		return true;
			
    	} catch (IOException e) {
    		log.error("Fail the move file. Move path [{} -> {}]", fromFilePath, toFilePath);
    		return false;
    	}
	}
    
	public String getExtractFolderPath() {
    	
		return picturePathProperties.getFromPath();
	}
    
    public void setPictureGeometry(PictureDto pictureDto, Geometry geometry) throws PictureDiaryException {
    	//1. declaration
    	String path = picturePathProperties.getDataPath(pictureDto.getPictureOriginName(), pictureDto.getExtension());

    	//2. open inputStream
    	try {
    		File inputFile = new File(path);
    		TiffOutputSet outputSet = null;
    		
    		//3. read metadata
    		final ImageMetadata metadata = Imaging.getMetadata(inputFile);
    		
    		final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
    		if (null != jpegMetadata) {
                final TiffImageMetadata exif = jpegMetadata.getExif();

                if (null != exif) {
                    outputSet = exif.getOutputSet();
                }
    		}
    		if (null == outputSet) {
                outputSet = new TiffOutputSet();
            }
    		
    		//4. gps metadata setting
            final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
            exifDirectory.removeField(ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
            exifDirectory.add(ExifTagConstants.EXIF_TAG_APERTURE_VALUE, new RationalNumber(3, 10));
            
            outputSet.setGPSInDegrees(geometry.getLongitude(), geometry.getLatitude());
           
            this.updateMetadata(path, outputSet);
            
    	} catch (ImageReadException e) {
			log.error("Metadata read failed. File Path [{}]", path);
			throw new PictureDiaryException(METADATA_READ_FAILED);
		} catch (FileNotFoundException e) {
			log.error("File not found. File Path [{}]", path);
			throw new PictureDiaryException(FILE_NOT_FOUND);
		} catch (IOException e) {
			log.error("IOEception occured.");
			throw new PictureDiaryException(ETC_EXCEPTION);
		} catch (ImageWriteException e) {
			log.error("Metadata write failed. File Path [{}]", path);
			throw new PictureDiaryException(METADATA_WRITE_FAILED);
		}
    }
    
    private void updateMetadata(String path,  TiffOutputSet outputSet) 
    		throws ImageWriteException, IOException, ImageReadException {
    	final File file = new File(path);
    	final BufferedImage img = Imaging.getBufferedImage(file);
    	byte[] imageBytes = Imaging.writeImageToBytes(img, ImageFormats.TIFF, new HashMap<>());
    	
    	final String tempPath = FilenameUtils.getBaseName(path) + "_temp." + FilenameUtils.getExtension(path);
    	final File ex = new File(tempPath);
    	
    	try(FileOutputStream fos = new FileOutputStream(ex);
    	    OutputStream os = new BufferedOutputStream(fos)) {
    	    new TiffImageWriterLossless(imageBytes).write(os, outputSet);
    	    
    		//Files.delete(Paths.get(path));
    		//Files.move(Paths.get(tempPath), Paths.get(path));
    	}
    }
    
    private Geometry getPictureGeometry(JpegImageMetadata metadata) {
    	try {
    		double latitude = metadata.getExif().getGPS().getLatitudeAsDegreesNorth();
			double longitude = metadata.getExif().getGPS().getLongitudeAsDegreesEast();

            return new Geometry(latitude, longitude);
    	} catch (ImageReadException | NullPointerException e) {
    		return null;
    	}
    }
    
    private LocalDateTime getPictureDate(JpegImageMetadata metadata) {
    	String pictureDate = "";
    	try {
    		pictureDate = metadata.getExif().getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL)[0];
			//Convert
    		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
    	     
            return LocalDateTime.parse(pictureDate, inputFormat);
    	} catch (ImageReadException | NullPointerException e) {
    		return null;
    	}
    }
}
