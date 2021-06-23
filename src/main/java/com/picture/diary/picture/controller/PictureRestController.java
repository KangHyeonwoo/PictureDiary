package com.picture.diary.picture.controller;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.service.PictureService;
import com.picture.diary.result.Result;
import com.picture.diary.result.Status;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
