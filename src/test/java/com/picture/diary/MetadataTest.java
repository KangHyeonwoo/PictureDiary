package com.picture.diary;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;

@SpringBootTest
public class MetadataTest {

	@Test
	void getAllMetadata() {
		String path = "C:/Users/KHW-IPC/Pictures/image/IMG_1580.JPG";
		
    	FileInputStream is = null;
    	
        try {
            is = new FileInputStream(path);
            Metadata metadata = ImageMetadataReader.readMetadata(is);
            
            metadata.getDirectories().forEach(directory -> {
            	
            	System.out.println(directory.getName());
            	directory.getTags().forEach(tag -> {
            		String tagName = tag.getTagName();
            		String description = tag.getDescription();
            		
            		System.out.println("	Tag Name : " + tagName + " , Description : " + description);
            	});
            });
            
        } catch (ImageProcessingException ie) {
        	ie.printStackTrace();
        } catch (IOException ie) {
        	ie.printStackTrace();
        } finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
