package com.picture.diary.extract.data;

public enum Extensions {
    HEIC,
    JPG,
    JPEG,
    PNG,
    NOT_ALLOWED;

    public static Extensions findOf(String fileName) {
        Extensions result;

        String[] fileNameElements = fileName.split(SplitParts.DOT.getValue());
        String extension = fileNameElements[fileNameElements.length -1];
        
        try {
            result = Extensions.valueOf(extension.toUpperCase());
        } catch (IllegalArgumentException ie) {
            result = Extensions.NOT_ALLOWED;
        }

        return result;
    }
    
}
