package com.picture.diary.extract.data;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Arrays;
import java.util.Locale;

@Slf4j
public enum Extensions {
    HEIC,
    JPG,
    JPEG,
    PNG,
    NOT_ALLOWED;

    public static boolean contains(String paramExtension) {
        Extensions result = Arrays.stream(Extensions.values())
                .filter(extension -> extension.toString().equals(paramExtension.toLowerCase()))
                .findAny()
                .orElse(Extensions.NOT_ALLOWED);

        return (result != Extensions.NOT_ALLOWED);
    }

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

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
