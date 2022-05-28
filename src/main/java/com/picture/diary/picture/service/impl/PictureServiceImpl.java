package com.picture.diary.picture.service.impl;

import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureEntity;
import com.picture.diary.picture.file.data.Picture;
import com.picture.diary.picture.file.service.PictureFileService;
import com.picture.diary.picture.repository.PictureRepository;
import org.springframework.stereotype.Service;

import com.picture.diary.picture.service.PictureService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final PictureFileService fileService;

    @Override
    public List<PictureDto> findNotDeletedListByUserId(String userId) {
        List<PictureEntity> pictureEntityList = pictureRepository.findListByUserId(userId);

        //DB 에서 조회했을 때 데이터가 있을 경우
        return pictureEntityList.stream()
                .filter(PictureEntity::isNotDeleted)
                .map(PictureEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PictureDto> findAllListByUserId(String userId) {
        List<PictureEntity> dbPictureList = pictureRepository.findListByUserId(userId);
        List<Picture> storePictureList = fileService.findAllListByUserId(userId);

        List<PictureDto> savePictureDtoList = new ArrayList<>();

        for(int i=0; i< storePictureList.size(); i++) {
            PictureDto storePictureDto = PictureDto.createBy(userId, storePictureList.get(i));
            boolean hasInDB = dbPictureList.stream()
                    .map(PictureEntity::toDto)
                    //TODO equals 재정의가 필요할까? (만약 해야 한다면 hashcode 도 같이 해야 함)
                    .anyMatch(dbPictureDto -> dbPictureDto.getPictureId() == storePictureDto.getPictureId() && dbPictureDto.getUserId().equals(storePictureDto.getUserId()));

            if(!hasInDB) {
                savePictureDtoList.add(storePictureDto);
            }
        }

        pictureRepository.saveAll(
                savePictureDtoList.stream()
                        .map(PictureDto::toEntity)
                        .collect(Collectors.toList())
        );

        return savePictureDtoList;
    }

    //TODO save 가 필요할까?
    @Override
    public PictureDto save(PictureDto pictureDto) {

        return pictureRepository.save(pictureDto.toEntity()).toDto();
    }

    @Override
    public void updateDescription(PictureDto pictureDto) {
        pictureRepository.save(pictureDto.toEntity());
    }

    @Override
    public void updateGeometry(PictureDto pictureDto) {
        pictureRepository.save(pictureDto.toEntity());
    }

    @Override
    public void delete(long pictureId) {
        PictureDto pictureDto = pictureRepository.findByPictureId(pictureId)
                .orElseThrow(() -> new PictureDiaryException(""))
                .toDto();

        pictureDto.updateToDelete();

        pictureRepository.save(pictureDto.toEntity());
    }
}
