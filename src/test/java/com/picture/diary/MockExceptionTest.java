package com.picture.diary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picture.diary.picture.controller.PictureRestController;
import com.picture.diary.picture.data.PictureRenameDto;
import com.picture.diary.picture.service.PictureService;

@WebMvcTest(PictureRestController.class)
public class MockExceptionTest {

	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	//컨트롤러에서 사용하는 bean 객체는 반드시 mockbean으로 등록해야 함
	@MockBean 
	PictureService pictureService;
	
	@Test
	@DisplayName("파일 이름 변경 길이 15자 이상의 경우 예외 발생시키기")
	void renameExceptionOfLong() throws Exception {
		long pictureId = 124;
		String pictureName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		
		PictureRenameDto dto = PictureRenameDto.builder()
				.pictureId(pictureId)
				.pictureName(pictureName)
				.build();
		
		String paramJson = objectMapper.writeValueAsString(dto);
		String url = "/pictures/ " + pictureId + " /pictureName";
		
		mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON)
				.content(paramJson))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	@DisplayName("파일 이름이 공백인 경우 예외 발생시키기")
	void renameExceptionOfBlack() throws Exception {
		long pictureId = 124;
		String pictureName = "          ";
		
		PictureRenameDto dto = PictureRenameDto.builder()
				.pictureId(pictureId)
				.pictureName(pictureName)
				.build();
		
		String paramJson = objectMapper.writeValueAsString(dto);
		String url = "/pictures/ " + pictureId + " /pictureName";
		
		mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON)
				.content(paramJson))
				.andExpect(status().is4xxClientError());
				
	}
}
