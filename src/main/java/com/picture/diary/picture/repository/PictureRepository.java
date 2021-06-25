package com.picture.diary.picture.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.picture.diary.picture.data.PictureEntity;

public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

	public Optional<PictureEntity> findByPictureId(long pictureId);

}
