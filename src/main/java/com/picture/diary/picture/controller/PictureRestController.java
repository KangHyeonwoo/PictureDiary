package com.picture.diary.picture.controller;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.service.PictureService;
import com.picture.diary.result.Result;
import com.picture.diary.result.Status;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class PictureRestController {

    private final PictureService pictureService;

    @PostMapping("/extract")
    public Result<List<PictureDto>> pictureExtract() {
    	List<PictureDto> resultList = pictureService.pictureExtract();
    	
        return Result.<List<PictureDto>>builder()
        		.status(Status.OK)
        		.responseData(resultList)
        		.build();
    }

    @GetMapping("/list")
    public Result<List<PictureDto>> findAllList() {
    	List<PictureDto> resultList = pictureService.findPictureList();
        
    	return Result.<List<PictureDto>>builder()
        		.status(Status.OK)
        		.responseData(resultList)
        		.build(); 
    }

    @PostMapping("///")
	public Result<PictureDto> rename(@RequestBody PictureDto pictureDto) {
		PictureDto result = pictureService.rename(pictureDto.getPictureId(), pictureDto.getPictureName());

		return Result.<PictureDto>builder()
				.status(Status.OK)
				.responseData(result)
				.build();
	}

	@PostMapping("////")
	public Result<PictureDto> addGeometry(@RequestBody PictureDto pictureDto) {
    	PictureDto result = pictureService.updateGeometry(
    			pictureDto.getPictureId(),
				pictureDto.getLatitude(),
				pictureDto.getLongitude());

		return Result.<PictureDto>builder()
				.status(Status.OK)
				.responseData(result)
				.build();
	}

	@PostMapping("/////")
	public Result<String> delete(@PathVariable("pictureId") long pictureId) {
		pictureService.delete(pictureId);

		String resultMessage = "";
		return Result.<String>builder()
				.status(Status.OK)
				.responseData(resultMessage)
				.build();
	}
}
