package com.picture.diary.picture.controller;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.service.PictureService;
import com.picture.diary.result.Result;
import com.picture.diary.result.ResultList;
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
    public ResultList<PictureDto> pictureExtract() {

        return pictureService.pictureExtract();
    }

    @GetMapping("/list")
    public ResultList<PictureDto> findAllList() {

        return pictureService.findPictureList();
    }



}
