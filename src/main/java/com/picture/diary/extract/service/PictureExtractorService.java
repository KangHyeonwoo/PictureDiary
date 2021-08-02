package com.picture.diary.extract.service;

import java.io.IOException;
import java.util.List;

import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.extract.data.Geometry;
import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.picture.data.PictureDto;

/**
 * @author KHW-IPC
 *
 */
public interface PictureExtractorService {

	/** 사진 목록 조회
	 * @param path
	 * @return List<PictureFile>
	 * @throws IOException
	 */
	List<PictureFile> getPictureList(String path);
	
	/** 사진 메타데이터 조회
	 * @param Picture Path
	 * @return PictureMetadata
	 */
	PictureMetadata getPictureMetadata(String path);
	
	
	/** 파일 이동
	 * @param String fromFilePath : 현재 파일 경로
	 * @param String toFilePath : 파일 이동할 경로
	 * @return
	 */
	boolean movePictureFile(String fromFilePath, String toFilePath) ;
	
	
	/** 중복확인
	 * @param pictureFile
	 * @param savedPictureList
	 * @return
	 */
	boolean doubleCheck(PictureFile pictureFile, List<PictureDto> savedPictureList);
	
	
	/** 추출 경로 조회
	 * @return
	 */
	String getExtractFolderPath();
	
	void setPictureGeometry(PictureDto pictureDto, Geometry geometry) throws PictureDiaryException;
}
