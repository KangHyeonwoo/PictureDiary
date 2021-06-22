package com.picture.diary.picture.repository;

import com.picture.diary.picture.data.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

	public PictureEntity findByPictureId(long pictureId);

}
