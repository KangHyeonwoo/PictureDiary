package com.picture.diary.picture.file.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 사진 객체
 */
@Getter
@ToString
public class Picture {

    private String name;

    private String path;

    private long size;

    private Geometry geometry;

    private LocalDateTime localDateTime;

    @Builder
    public Picture(String name, String path, long size, Geometry geometry, LocalDateTime localDateTime) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.geometry = geometry;
        this.localDateTime = localDateTime;
    }

    public Picture(File file) {

    }

    public boolean hasMetadata() {
        return this.geometry == null && this.localDateTime == null;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Picture picture = (Picture) o;

        return name.equalsIgnoreCase(picture.name) &&
                path.equalsIgnoreCase(picture.path) &&
                size == picture.size &&
                geometry.equals(picture.geometry) &&
                localDateTime.equals(picture.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, size, geometry, localDateTime);
    }
}
