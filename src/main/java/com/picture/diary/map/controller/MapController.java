package com.picture.diary.map.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.picture.diary.picture.data.PictureDto;
import com.picture.diary.picture.service.PictureService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MapController {

	private final PictureService pictureService;
	
    @GetMapping("/")
    public String mapView() {

        return "pictureMap";
    }

    @GetMapping("/infowindow/{pictureId}")
    public String infowindowView(@PathVariable("pictureId") long pictureId, Model model) {
    	PictureDto result = pictureService.findByPictureId(pictureId);
    	
    	model.addAttribute("pictureDto", result);
    	
    	return "infowindow/infowindow";
    }
}
