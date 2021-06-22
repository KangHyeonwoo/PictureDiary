package com.picture.diary.picture.service.impl;

import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.extract.service.PictureExtractorService;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureEntity;
import com.picture.diary.picture.repository.PictureRepository;
import com.picture.diary.picture.service.PictureService;
import com.picture.diary.result.Result;
import com.picture.diary.result.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService {

    private final PicturePathProperties picturePathProperties;
    private final PictureExtractorService pictureExtractorService;
    private final PictureRepository pictureRepository;

    public Result<PictureDto> findByPictureId(long pictureId) {
    	PictureEntity pictureEntity = pictureRepository.findByPictureId(pictureId);
    	
    	return Result.<PictureDto>builder()
    			.status(Status.OK)
    			.responseData(pictureEntity.toDto()).build();
    }
    
    public Result<List<PictureDto>> pictureExtract() {
        String path = picturePathProperties.getFromPath();
        //1. 사진파일 목록 조회
        List<PictureFile> pictureFileList = pictureExtractorService.getPictureList(path);

        List<PictureEntity> pictureEntityList = pictureFileList.stream()
                .map(pictureFile -> {
                    //2. 메타데이터 추출
                    PictureMetadata metadata = pictureExtractorService.getPictureMetadata(pictureFile);
                    pictureFile.addMetadata(metadata);

                    //3. 디렉토리 이동
                    //3-1. 좌표정보가 없을 경우 temp 로 이동
                    if(pictureFile.getPictureMetadata().getGeometry() == null) {
                        pictureExtractorService.movePictureToTempPath(pictureFile);
                    //3-2. 좌표정보가 있을 경우 data 로 이동
                    } else {
                        pictureExtractorService.movePictureToDataPath(pictureFile);
                    }
                    return pictureFile;
                })
                //4. DB에 저장하기 위해 entity 로 형변환
                .map(pictureFile -> pictureFile.toEntity())
                .collect(Collectors.toList());
        //5. DB에 저장
        List<PictureEntity> savedList = pictureRepository.saveAll(pictureEntityList);
        List<PictureDto> savedDtoList = savedList.stream()
                .map(PictureEntity::toDto)
                .collect(Collectors.toList());

        return Result.<List<PictureDto>>builder()
                .status(Status.OK)
                .responseData(savedDtoList)
                .build();
    }

    public Result<List<PictureDto>> findPictureList() {

        List<PictureDto> pictureDtoList = pictureRepository.findAll().stream()
                .map(PictureEntity::toDto)
                .collect(Collectors.toList());

        return Result.<List<PictureDto>>builder()
                .status(Status.OK)
                .responseData(pictureDtoList).build();
    }

}
