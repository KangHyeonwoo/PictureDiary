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

import com.picture.diary.picture.controller.PictureRestController;
import com.picture.diary.picture.data.PictureRenameDto;
import com.picture.diary.picture.service.PictureService;

@WebMvcTest(PictureRestController.class)
public class MockExceptionTest {

	@Autowired
	MockMvc mvc;
	
	@MockBean
	PictureService pictureService;
	
	@Test
	@DisplayName("파일 이름 변경 예외 발생시키기")
	void rename() throws Exception {
		//1. 특수문자가 있는 경우
		//2. 길이가 15글자 이상인 경우
		//3. 파일이름이 공백인 경우(trim 기준)
		long pictureId = 124;
		String pictureName = "";
		
		PictureRenameDto dto = PictureRenameDto.builder()
				.pictureId(pictureId)
				.pictureName(pictureName)
				.build();
		
		
		
		String content = "";
		mvc.perform(put("/").contentType(MediaType.APPLICATION_JSON)
				.content(content))
				.andExpect(status().isCreated());
		
			
	}
}
