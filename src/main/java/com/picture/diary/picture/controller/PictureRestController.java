package com.picture.diary.picture.controller;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.service.PictureService;
import com.picture.diary.result.Result;
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

        return pictureService.pictureExtract();
    }

    @GetMapping("/list")
    public Result<List<PictureDto>> findAllList() {

        return pictureService.findPictureList();
    }



}
