package com.picture.diary.picture.controller;

import com.picture.diary.extract.data.Geometry;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.service.PictureService;
import com.picture.diary.response.BasicResponse;
import com.picture.diary.response.ErrorResponse;
import com.picture.diary.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class PictureRestController {

    private final PictureService pictureService;
    
    @GetMapping("")
    public ResponseEntity<BasicResponse> findAllList() {
    	List<PictureDto> resultList = pictureService.findPictureList();
        
    	return ResponseEntity.ok()
    			.body(new SuccessResponse<List<PictureDto>>(resultList));
    }
    
    @PostMapping("/extract")
    public ResponseEntity<BasicResponse> pictureExtract() {
    	List<PictureDto> resultList = pictureService.pictureExtract();
    	
    	return ResponseEntity.ok()
    			.body(new SuccessResponse<List<PictureDto>>(resultList));
    }

    @PatchMapping("/{pictureId}/pictureName")
	public ResponseEntity<BasicResponse> rename(@PathVariable("pictureId") long pictureId, @RequestBody PictureDto pictureDto) {
    	PictureDto result = pictureService.rename(pictureId, pictureDto.getPictureName());
    	
		return ResponseEntity.ok().body(new SuccessResponse<PictureDto>(result));
	}

	@PatchMapping("/{pictureId}/geometry")
	public ResponseEntity<BasicResponse> addGeometry(@PathVariable("pictureId") long pictureId, @RequestBody Geometry geometry) {
		PictureDto result = pictureService.updateGeometry(
				    			pictureId,
								geometry.getLatitude(),
								geometry.getLongitude());
		
		return ResponseEntity.ok().body(new SuccessResponse<PictureDto>(result));
	}

	@DeleteMapping("/{pictureId}")
	public ResponseEntity<BasicResponse> deletePicture(@PathVariable("pictureId") long pictureId) {
		pictureService.delete(pictureId);
		
		return ResponseEntity.ok().body(new SuccessResponse<String>());
	}
}
