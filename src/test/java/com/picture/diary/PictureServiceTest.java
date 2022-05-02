package com.picture.diary;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureEntity;
import com.picture.diary.picture.file.data.Extensions;
import com.picture.diary.picture.file.data.Picture;
import com.picture.diary.picture.repository.PictureRepository;
import com.picture.diary.picture.service.PictureService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  /test/resources/images
 *      - 11546.JPEG    -> geometry, localDateTime
 *      - IMG_1822.JPG  -> geometry, localDateTime
 *      - IMG_2073.HEIC -> localDateTime
 *      - IMG_2074.HEIC -> geometry, localDateTime
 */
@SpringBootTest
public class PictureServiceTest {

    @Autowired
    PictureService pictureService;

    @Autowired
    PictureRepository pictureRepository;

    @Test
    @DisplayName("DB에 사진 정보 저장 테스트")
    void savePictureDBTest() {
        String fileName = "IMG_2074";
        File testFile = Paths.get("src", "test","resources","images", "IMG_2074.HEIC").toFile();
        
        Picture picture = new Picture(testFile);
        PictureDto saveDto = PictureDto.createBy("hwkang", picture);
        pictureRepository.save(saveDto.toEntity());

        List<PictureDto> list = pictureService.findNotDeletedListByUserId("hwkang");

        boolean result = list.stream()
                .anyMatch(pictureDto -> fileName.equals(pictureDto.getPictureName()));

        Assertions.assertThat(result).isTrue();
    }

    /*
        저장소에서 전체 데이터 조회하는 테스트 경우의 수
        1. 저장소에는 있고 DB 에는 없을 경우, 해당 데이터 DB 에 추가 후 리턴
        2. 저장소에서 조회한 모든 데이터가 DB 에 있을 경우 아무것도 리턴하지 않음
        3. 사용자 ID 다를 경우 결과 제대로 조회되는지 체크해봐야함
     */

    @Test
    @Transactional
    @DisplayName("저장소에는 있고 DB 에는 없을 경우, 해당 데이터는 DB 에 추가 후 리턴한다. (findAllListByUserId)")
    void test() throws Exception {
        //1. given - 테스트 파일 중 좌표정보가 있는 파일 2개 DB 에 저장
        final String userId = "hwkang";
        File file1 = Paths.get("src", "test", "resources", "images", "IMG_2074.HEIC").toFile();
        File file2 = Paths.get("src", "test", "resources", "images", "IMG_1822.JPG").toFile();

        PictureEntity pictureEntity1 = PictureDto.createBy(userId, new Picture(file1)).toEntity();
        PictureEntity pictureEntity2 = PictureDto.createBy(userId, new Picture(file2)).toEntity();

        pictureRepository.saveAll(List.of(pictureEntity1, pictureEntity2));

        //2. when - findAllListByUserId 메인 로직 테스트
        File file = Paths.get("src", "test", "resources", "images", "11546.JPEG").toFile();

        //2-1. 테스트 데이터 세팅
        List<PictureEntity> dbPictureList = pictureRepository.findListByUserId(userId);
        List<Picture> filePictureList = List.of(new Picture(file));

        List<PictureDto> savePictureDtoList = new ArrayList<>();

        //2-2. 저장소 파일 목록을 루프문 돌리면서 DB 에 저장된 파일과 일치하는지 조회
        for(int i=0; i< filePictureList.size(); i++) {
            PictureDto filePictureDto = PictureDto.createBy(userId, filePictureList.get(i));
            //DB 에 저장된 파일과 일치하는가?
            boolean hasInDB = dbPictureList.stream()
                    .map(PictureEntity::toDto)
                    .anyMatch(dbPictureDto -> dbPictureDto.getPictureId() == filePictureDto.getPictureId() && dbPictureDto.getUserId().equals(filePictureDto.getUserId()));

            //일치하지 않는다면 (DB 에 저장된 사진이 아니라면) 새롭게 저장할 배열에 추가한다.
            if(!hasInDB) {
                savePictureDtoList.add(filePictureDto);
            }
        }

        //3. then - 결과 확인
        //3-1. 새로 저장할 사진 목록(saveDtoList) 는 1개여야 한다.
        Assertions.assertThat(savePictureDtoList.size()).isEqualTo(1);

        //2-3. 배열을 Entity 클래스로 변환하고 저장한다.
        pictureRepository.saveAll(
                savePictureDtoList.stream()
                        .map(PictureDto::toEntity)
                        .collect(Collectors.toList())
        );

        //3-2. DB 에 저장된 사진 목록은 3개여야 한다.
        List<PictureDto> notDeletedListByUserId = pictureService.findNotDeletedListByUserId(userId);

        Assertions.assertThat(notDeletedListByUserId.size()).isEqualTo(3);
    }
}
