package com.picture.diary.picture.controller;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.service.PictureService;
import com.picture.diary.response.BasicResponse;
import com.picture.diary.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class PictureRestController {

    private final PictureService pictureService;

    @PostMapping("/extract")
    public ResponseEntity<BasicResponse> pictureExtract() {
    	List<PictureDto> resultList = pictureService.pictureExtract();
    	
    	return ResponseEntity.ok()
    			.body(new SuccessResponse<List<PictureDto>>(resultList));
    }

    @GetMapping("/list")
    public ResponseEntity<BasicResponse> findAllList() {
    	List<PictureDto> resultList = pictureService.findPictureList();
        
    	return ResponseEntity.ok()
    			.body(new SuccessResponse<List<PictureDto>>(resultList));
    }

    @PostMapping("/rename")
	public ResponseEntity<BasicResponse> rename(@RequestBody PictureDto pictureDto) {
		PictureDto result = pictureService.rename(pictureDto.getPictureId(), pictureDto.getPictureName());

		return ResponseEntity.ok()
				.body(new SuccessResponse<PictureDto>(result));
	}

	@PostMapping("/addGeometry")
	public ResponseEntity<BasicResponse> addGeometry(@RequestBody PictureDto pictureDto) {
    	PictureDto result = pictureService.updateGeometry(
				    			pictureDto.getPictureId(),
								pictureDto.getLatitude(),
								pictureDto.getLongitude());

		return ResponseEntity.ok()
				.body(new SuccessResponse<PictureDto>(result));
	}

	@PostMapping("/{pictureId}/delete")
	public ResponseEntity<BasicResponse> delete(@PathVariable("pictureId") long pictureId) {
		pictureService.delete(pictureId);
		String resultMessage = "";
		
		return ResponseEntity.ok()
				.body(new SuccessResponse<String>(resultMessage));
	}
	
	
}
