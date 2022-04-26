package com.picture.diary.picture.service.impl;

import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureEntity;
import com.picture.diary.picture.file.service.PictureFileService;
import com.picture.diary.picture.repository.PictureRepository;
import org.springframework.stereotype.Service;

import com.picture.diary.picture.service.PictureService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final PictureFileService fileService;

    //TODO Cache -> 사용하는 곳 많다면 AOP 처리 가능하지 않을까
    private final Map<String, List<PictureDto>> cacheMap = new HashMap<>();

    @Override
    public List<PictureDto> findListByUserId(String userId) {
        List<PictureDto> cacheList = cacheMap.get(userId);
        if(cacheList != null && !cacheList.isEmpty()) {
            return cacheList;
        }

        List<PictureEntity> pictureEntityList = pictureRepository.findListByUserId(userId);
        //TODO pictureEntityList 결과가 0건인 경우 null 인가 Empty List 인가?
        return pictureEntityList.stream()
                .filter(PictureEntity::isNotDeleted)
                .map(PictureEntity::toDto)
                .collect(Collectors.toList());
    }

    //TODO save 가 필요할까?
    @Override
    public PictureDto save(PictureDto pictureDto) {
        pictureRepository.save(pictureDto.toEntity());
        return null;
    }

    @Override
    public void updateDescription(PictureDto pictureDto) {
        PictureEntity savedEntity = pictureRepository.save(pictureDto.toEntity());

        PictureDto savedDto = savedEntity.toDto();
        //TODO 주석 풀기
        //String userId = pictureDto.getUserId();
        String userId = "";
        List<PictureDto> cacheList = cacheMap.get(userId);

        PictureDto updateDto = cacheList.stream()
                .filter(o -> o.getPictureId() == savedDto.getPictureId())
                .findFirst()
                .orElseThrow(() -> new PictureDiaryException(""));

        boolean remove = cacheList.remove(updateDto);

        if(remove) {
            cacheList.add(updateDto);
        } else {
            throw new PictureDiaryException("");
        }
    }

    @Override
    public void updateGeometry(PictureDto pictureDto) {

        //TODO 위와 같으므로 우선 패스
    }

    @Override
    public void delete(long pictureId) {
        boolean deleted = pictureRepository.updateDeleteYn(pictureId, "Y");

        //TODO Cache 처리
    }
}
