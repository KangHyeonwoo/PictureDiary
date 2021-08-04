package com.picture.diary;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.picture.diary.extract.data.Extensions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.picture.diary.extract.data.PictureFile;
import com.picture.diary.extract.data.PictureMetadata;
import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.extract.service.PictureExtractService;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureEntity;
import com.picture.diary.picture.repository.PictureRepository;
import com.picture.diary.picture.service.PictureService;

@SpringBootTest
public class PictureTest {

    @Autowired
    PictureRepository pictureRepository;

    @Autowired
    PictureExtractService pictureExtractorService;

    @Autowired
    PicturePathProperties picturePathProperties;

    @Autowired
    PictureService pictureService;
    
    @Test
    void databaseConnectTest() {
        List<PictureEntity> pictureEntityList = pictureRepository.findAll();

        Assertions.assertThat(pictureEntityList.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    void saveTest() {
        String path = picturePathProperties.getFromPath();

        List<PictureFile> pictureList = pictureExtractorService.getPictureList(path);
        List<PictureEntity> pictureEntityList = pictureList.stream()
                .map(pictureFile -> {
                	String fromPath = picturePathProperties.getFromPath(pictureFile.getFileName(), pictureFile.getExtension());
                	
                    PictureMetadata metadata = pictureExtractorService.getPictureMetadata(fromPath);
                    pictureFile.addMetadata(metadata);

                    return pictureFile;
                })
                .map(pictureFile -> pictureFile.toEntity())
                .collect(Collectors.toList());
        int beforeSaveSize = pictureEntityList.size();
        pictureRepository.saveAll(pictureEntityList);

        List<PictureEntity> resultList =  pictureRepository.findAll();
        int selectListSize = resultList.size();

        resultList.forEach(System.out::println);

        Assertions.assertThat(beforeSaveSize).isEqualTo(selectListSize);
    }
    
    @Test
    @Transactional
    void renameTest() {
    	long pictureId = 1;
    	PictureDto pictureDto = pictureService.findByPictureId(pictureId);
    	String beforePictureName = pictureDto.getPictureName();
    	PictureDto afterPictureDto = pictureService.rename(pictureId, "rename");
    	String afterPictureName = afterPictureDto.getPictureName();
    	
    	Assertions.assertThat(beforePictureName).isNotEqualTo(afterPictureName);
    }
    
    @Test
    @Transactional
    void deleteTest() {
    	long pictureId = 1;
    	pictureService.delete(pictureId);
    	
    	PictureDto pictureDto = pictureService.findByPictureId(pictureId);
    	Assertions.assertThat(pictureDto).isNull();
    }

    @Test
    @Transactional
    @DisplayName("DTO 객체 만들어서 저장하기(toEntity() 시 id값 널로 들어가는지 확인하기 위함)")
    void saveTest2() {
        String pictureOriginName = "TEST";
        Extensions extension = Extensions.JPEG;
        long pictureSize = 1;
        String picturePath ="/test/";

        PictureDto pictureDto = PictureDto.builder()
                .pictureOriginName(pictureOriginName)
                .extension(extension)
                .pictureSize(pictureSize)
                .build();

        PictureDto savedDto = pictureService.save(pictureDto);
        Assertions.assertThat(savedDto.getPictureId()).isGreaterThan(0);
    }
}
