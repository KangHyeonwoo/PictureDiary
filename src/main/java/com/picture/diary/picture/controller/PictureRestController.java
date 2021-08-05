package com.picture.diary.picture.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picture.diary.extract.data.Geometry;
import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.data.PictureRenameDto;
import com.picture.diary.picture.service.PictureService;
import com.picture.diary.response.BasicResponse;
import com.picture.diary.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class PictureRestController {

    private final PictureService pictureService;
    
    @GetMapping
    public ResponseEntity<BasicResponse> findAllList() {
    	List<PictureDto> resultList = pictureService.findAllPictureList();
        
    	return ResponseEntity.ok()
    			.body(new SuccessResponse<List<PictureDto>>(resultList));
    }

	@DeleteMapping("/{pictureId}")
	public ResponseEntity<BasicResponse> deletePicture(@PathVariable("pictureId") long pictureId) {
		pictureService.delete(pictureId);
		
		return ResponseEntity.ok().body(new SuccessResponse<String>());
	}
	
    @PutMapping("/{pictureId}/pictureName")
   	public ResponseEntity<BasicResponse> rename(@PathVariable("pictureId") long pictureId, 
   			@Valid @RequestBody PictureRenameDto pictureRenameDto) {
    	
    	//TODO pictureId , pictureRenameDto.getPictureId() 같은지 검사하기
    	
       	PictureDto result = pictureService.rename(pictureRenameDto);
       	
   		return ResponseEntity.ok().body(new SuccessResponse<PictureDto>(result));
   	}


    //MEMO : Geometry -> Location(Geometry + Address)
	@PutMapping("/{pictureId}/location")
	public ResponseEntity<BasicResponse> updateGeometry(@PathVariable("pictureId") long pictureId, 
			@RequestBody Geometry geometry) {
		PictureDto result = pictureService.updateGeometry(
				    			pictureId,
								geometry.getLatitude(),
								geometry.getLongitude());
		
		return ResponseEntity.ok().body(new SuccessResponse<PictureDto>(result));
	}
	
	@PutMapping("/address")
	public ResponseEntity<BasicResponse> updateAddressList(@RequestBody List<?> pictureAddressDtoList) {
		
		return null;
	}
}