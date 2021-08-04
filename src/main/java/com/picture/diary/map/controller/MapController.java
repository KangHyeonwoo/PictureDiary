package com.picture.diary.map.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.picture.diary.extract.service.PictureExtractorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MapController {

	private final PictureExtractorService pictureExtractService;
	
    @GetMapping("/")
    public String mapView(Model model) {
    	String path = pictureExtractService.getExtractFolderPath();
    	model.addAttribute("path", path);
        return "pictureMap";
    }
    
    @GetMapping("/toc")
    public String tocView() {
    	return "toc";
    }
}
