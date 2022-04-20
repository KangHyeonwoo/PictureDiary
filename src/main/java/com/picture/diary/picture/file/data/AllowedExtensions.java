package com.picture.diary.picture.file.data;

/**
 *  추출 가능한 확장자
 */
public enum AllowedExtensions {

    JPEG, HEIC;

    /**
     * 파일 확장자 체크 여부
     *
     * @param fileName
     * @return
     */
    public static boolean contains(String fileName) {
        String separator = "[.]";

        String[] parts = fileName.split(separator);

        if(parts.length == 0) {
            return false;
        }

        String compareExtension = parts[parts.length - 1];

        for(AllowedExtensions allowedExtension : AllowedExtensions.values()) {
            if(allowedExtension.name().equalsIgnoreCase(compareExtension)) {
                return true;
            }
        }

        return false;
    }
}
