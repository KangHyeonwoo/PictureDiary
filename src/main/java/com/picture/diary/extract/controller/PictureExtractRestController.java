package com.picture.diary.extract.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picture.diary.extract.data.Geometry;
import com.picture.diary.extract.service.PictureExtractService;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureRenameDto;
import com.picture.diary.picture.service.PictureService;
import com.picture.diary.response.BasicResponse;
import com.picture.diary.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/extract")
@RequiredArgsConstructor
public class PictureExtractRestController {

	private final PictureService pictureService;
    private final PictureExtractService pictureExtractService;
 
    @PostMapping
    public ResponseEntity<BasicResponse> pictureExtract() {
    	//중복확인을 위해 DB에 저장돼있는 파일 리스트 조회
    	List<PictureDto> savedPictureDtoList = pictureService.findAllPictureList();
    	
    	List<PictureDto> resultList = pictureExtractService.pictureExtract(savedPictureDtoList);
    	return ResponseEntity.ok()
    			.body(new SuccessResponse<List<PictureDto>>(resultList));
    }
}
