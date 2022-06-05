package com.picture.diary.picture.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.picture.diary.picture.data.PictureEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

	List<PictureEntity> findListByUserId(String userId);

	Optional<PictureEntity> findByPictureId(long pictureId);

	//boolean updateDeleteYn(long pictureId, String deleteYn);

}
