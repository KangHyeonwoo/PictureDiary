package com.picture.diary.picture.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.extract.service.PictureExtractorService;
import com.picture.diary.picture.data.InfowindowDto;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureEntity;
import com.picture.diary.picture.exception.PictureException;
import com.picture.diary.picture.repository.PictureRepository;
import com.picture.diary.picture.service.PictureService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService {

    private final PicturePathProperties picturePathProperties;
    private final PictureExtractorService pictureExtractorService;
    private final PictureRepository pictureRepository;

    public PictureDto findByPictureId(long pictureId) {
    	
    	return pictureRepository.findByPictureId(pictureId)
    			.map(PictureEntity::toDto)
    			.orElseThrow(() -> new PictureException("picture not found."));
    }
    
    public List<PictureDto> pictureExtract() {
        String path = picturePathProperties.getFromPath();
        //1. 사진파일 목록 조회
        List<PictureFile> pictureFileList = pictureExtractorService.getPictureList(path);
        
        List<PictureDto> savedPictureList = this.findPictureList();
        
        List<PictureEntity> pictureEntityList = pictureFileList.stream()
        		.filter(pictureFile -> !pictureExtractorService.doubleCheck(pictureFile, savedPictureList))
                .filter(pictureFile -> {
                	
                    //2. 메타데이터 추출
                    PictureMetadata metadata = pictureExtractorService.getPictureMetadata(pictureFile);
                    pictureFile.addMetadata(metadata);
                    
                    //3. 디렉토리 이동
                    String fromPath = pictureFile.getFilePath();
                    String toPath = picturePathProperties.getDataPath(pictureFile);
                    
                    return pictureExtractorService.movePictureFile(fromPath, toPath);
                })
                //4. DB에 저장하기 위해 entity 로 형변환
                .map(pictureFile -> {
                	String toPath = picturePathProperties.getDataPath(pictureFile);
                	pictureFile.changeFilePath(toPath);
                	
                	return pictureFile.toEntity();
                })
                .collect(Collectors.toList());
        
        //5. DB에 저장
        List<PictureEntity> savedList = pictureRepository.saveAll(pictureEntityList);
        List<PictureDto> savedDtoList = savedList.stream()
                .map(PictureEntity::toDto)
                .collect(Collectors.toList());
        log.info("picture extract success [saved list : {} , files list : {}]", savedDtoList.size(), pictureFileList.size());
        return savedDtoList;
    }

    public List<PictureDto> findPictureList() {
        List<PictureDto> pictureDtoList = pictureRepository.findAll().stream()
                .map(PictureEntity::toDto)
                .collect(Collectors.toList());

        return pictureDtoList;
    }

    public PictureDto rename(long pictureId, String pictureName) {
        PictureDto pictureDto = this.findByPictureId(pictureId);
        pictureDto.rename(pictureName);

        return this.save(pictureDto);
    }

    public PictureDto updateGeometry(long pictureId, double latitude, double longitude) {
        PictureDto pictureDto = this.findByPictureId(pictureId);
        pictureDto.updateGeometry(latitude, longitude);

        String fromPath = pictureDto.getPicturePath();
        
        //신규로 좌표를 추가하는 경우 해당 파일 data 디렉토리 이동
        if(fromPath.contains(picturePathProperties.getFromPath())) {
        	String toPath = picturePathProperties.getDataPath() + "/" + pictureDto.getPictureOriginName() + "." + pictureDto.getExtension();
        	pictureExtractorService.movePictureFile(fromPath, toPath);
        }
        
        return this.save(pictureDto);
    }

    public void delete(long pictureId) {
    	Optional<PictureEntity> pictureEntity = Optional.of(pictureRepository.findByPictureId(pictureId)
    			.orElseThrow(() -> new IllegalArgumentException()));
    	PictureDto pictureDto = pictureEntity.get().toDto();
    	
    	//파일 delete 디렉토리로 이동
    	String fromPath = pictureDto.getPicturePath();
    	String toPath = picturePathProperties.getDeletePath() + "/" + pictureDto.getPictureOriginName() + "." + pictureDto.getExtension();
    	pictureExtractorService.movePictureFile(fromPath, toPath);
    	
        pictureRepository.delete(pictureEntity.get());
    }

    public PictureDto save(PictureDto pictureDto) {
        PictureEntity savedEntity = pictureRepository.save(pictureDto.toEntity());

        return savedEntity.toDto();
    }
    
    public InfowindowDto findInfowindowByPictureId(long pictureId) {
    	PictureDto pictureDto = this.findByPictureId(pictureId);
    	String pictureName = (StringUtils.hasLength(pictureDto.getPictureName()) ? pictureDto.getPictureName() : pictureDto.getPictureOriginName());
    	
    	return InfowindowDto.builder()
    			.pictureId(pictureDto.getPictureId())
    			.pictureName(pictureName)
    			.pictureDate(pictureDto.getPictureDate())
    			.pictureOriginName(pictureDto.getPictureOriginName())
    			.extension(pictureDto.getExtension())
    			.build();
    }
}
