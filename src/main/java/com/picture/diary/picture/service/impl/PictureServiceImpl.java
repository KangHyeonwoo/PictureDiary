package com.picture.diary.picture.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.base.Objects;
import com.picture.diary.extract.data.Geometry;
import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.extract.service.PictureExtractorService;
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
        		.map(pictureFile -> {
        			String fromPath = picturePathProperties.getFromPath(pictureFile.getFileName(), pictureFile.getExtension());
        			//2. 메타데이터 추출
        			//	- 메타데이터 중 속성정보가 하나도 없으면 new PictureMetadata()를 리턴함.
        			//  - 메타데이터 추출 중 오류 발생하면 metadata는 null을 리턴함.
                    PictureMetadata metadata = pictureExtractorService.getPictureMetadata(fromPath);
                    pictureFile.addMetadata(metadata);
                    
                    return pictureFile;
        		})
        		//3. 메타데이터 추출 중 오류 발생한 데이터는 추출 목록에서 제거
        		.filter(pictureFile -> pictureFile.getPictureMetadata() != null)
                .filter(pictureFile -> {
                	String fromPath = picturePathProperties.getFromPath(pictureFile.getFileName(), pictureFile.getExtension());
                    //4. 디렉토리 이동
                    String toPath = picturePathProperties.getDataPath(pictureFile.getFileName(), pictureFile.getExtension());
                    
                    return pictureExtractorService.movePictureFile(fromPath, toPath);
                })
                //5. 파일 이동 성공한 데이터들을 entity 로 형변환
                .map(PictureFile::toEntity)
                .collect(Collectors.toList());
        
        //6. DB에 저장
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
        
        //TODO 여기 속도 개선 필요함
        
        /**
         * 새로 좌표 추가하면 빨리 되는데
         * 기존에 좌표가 있는 경우 느림
         * 같은 메소드 타는데 속도가 차이나는 이유가 뭘까
         * setPictureGeometry > getOrCreateExifDirectory() 얘의 차이일 수 있겠다.
         * 없으면 새로 만들고 있으면 기존거 다 조회해야하니까
         * 속도 개선 방안 1
         *  - DB만 먼저 갱신해서 지도에는 데이터 이동한 것 처럼 보이고, 그 다음에 파일 메타데이터 변경하기
         * 
         */
        //TODO 원본 데이터 유지 위해 잠깐 주석
        //pictureExtractorService.setPictureGeometry(pictureDto, new Geometry(latitude, longitude));
        
        pictureDto.updateGeometry(latitude, longitude);
        
        return this.save(pictureDto);        	
        
    }

    public void delete(long pictureId) {
    	Optional<PictureEntity> pictureEntity = Optional.of(pictureRepository.findByPictureId(pictureId)
    			.orElseThrow(() -> new IllegalArgumentException()));
    	PictureDto pictureDto = pictureEntity.get().toDto();
    	
    	//파일 delete 디렉토리로 이동
    	String fromPath = picturePathProperties.getDataPath(pictureDto.getPictureOriginName(), pictureDto.getExtension());
    	String toPath = picturePathProperties.getDeletePath(pictureDto.getPictureOriginName(), pictureDto.getExtension());
    	pictureExtractorService.movePictureFile(fromPath, toPath);
    	
        pictureRepository.delete(pictureEntity.get());
    }

    public PictureDto save(PictureDto pictureDto) {
        PictureEntity savedEntity = pictureRepository.save(pictureDto.toEntity());
        
        return savedEntity.toDto();
    }
}
