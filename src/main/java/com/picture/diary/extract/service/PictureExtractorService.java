package com.picture.diary.extract.service;

import java.io.IOException;
import java.util.List;

import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.picture.data.PictureDto;

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
	
	
	/** 파일 이동
	 * @param pictureFile
	 * @return
	 */
	boolean movePictureFile(PictureFile pictureFile) ;
	
	
	/** 중복확인
	 * @param pictureFile
	 * @param savedPictureList
	 * @return
	 */
	boolean doubleCheck(PictureFile pictureFile, List<PictureDto> savedPictureList);
	
	//TODO 이미지 파일 삭제
	
}
