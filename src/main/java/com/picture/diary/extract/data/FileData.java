package com.picture.diary.extract.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.File;

@Builder
@AllArgsConstructor
@Getter
public class FileData {

    private String fileName;
    private String extension;
    private long fileSize;

    private String filePath;
    private ImageMetadata imageMetadata;

    public FileData(File file) {
        String fileName = file.getName();

        this.fileName = fileName;
        this.extension = Extensions.findOf(fileName).toString();
        this.fileSize = file.length();
        this.filePath = file.getPath();
    }
}
