package com.picture.diary.picture.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.extract.data.Geometry;
import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.extract.exception.PictureExtractExceptionType;
import com.picture.diary.extract.service.PictureExtractService;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureEntity;
import com.picture.diary.picture.data.PictureRenameDto;
import com.picture.diary.picture.repository.PictureRepository;
import com.picture.diary.picture.service.PictureService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService {

    private final PicturePathProperties picturePathProperties;
    private final PictureExtractService pictureExtractService;
    private final PictureRepository pictureRepository;

    public PictureDto findByPictureId(long pictureId) {
    	
    	return pictureRepository.findByPictureId(pictureId)
    			.map(PictureEntity::toDto)
    			.orElseThrow(() -> new PictureDiaryException(PictureExtractExceptionType.FILE_NOT_FOUND));
    }
    
    public List<PictureDto> findAllPictureList() {
        List<PictureDto> pictureDtoList = pictureRepository.findAll().stream()
                .map(PictureEntity::toDto)
                .collect(Collectors.toList());

        return pictureDtoList;
    }

    public PictureDto rename(PictureRenameDto pictureRenameDto) {
    	final long pictureId = pictureRenameDto.getPictureId();
    	final String pictureName = pictureRenameDto.getPictureName();
    	
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
        
        pictureExtractService.setPictureGeometry(pictureDto, new Geometry(latitude, longitude));
        
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
    	pictureExtractService.movePictureFile(fromPath, toPath);
    	
        pictureRepository.delete(pictureEntity.get());
    }

    public PictureDto save(PictureDto pictureDto) {
        PictureEntity savedEntity = pictureRepository.save(pictureDto.toEntity());
        
        return savedEntity.toDto();
    }
}
