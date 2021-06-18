package com.picture.diary.extract.service;

import java.io.IOException;
import java.util.List;

import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;

public interface PictureExtractorService {

	
	
	/** 사진 목록 조회
	 * @param path
	 * @return List<PictureFile>
	 * @throws IOException
	 */
	List<PictureFile> getPictureList(String path);
	
	/** 사진 메타데이터 조회
	 * @param pictureFile
	 * @return PictureMetadata
	 */
	PictureMetadata getPictureMetadata(PictureFile pictureFile);
	
	/** 사진 데이터 디렉토리로 이동
	 * @param pictureFile
	 * @return boolean
	 */
	boolean movePictureToDataPath(PictureFile pictureFile);
	
	/** 사진 임시 디렉토리로 이동
	 * @param pictureFile
	 * @return boolean
	 */
	boolean movePictureToTempPath(PictureFile pictureFile);
	
	//TODO 이미지 파일 삭제
	
}
