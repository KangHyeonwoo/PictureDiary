package com.picture.diary.extract.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 사진 객체
 */
@Getter
@ToString
public class Picture {

    private String name;

    private long size;

    private Geometry geometry;

    private LocalDateTime localDateTime;

    @Builder
    public Picture(String name, long size, Geometry geometry, LocalDateTime localDateTime) {
        this.name = name;
        this.size = size;
        this.geometry = geometry;
        this.localDateTime = localDateTime;
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
                size == picture.size &&
                geometry.equals(picture.geometry) &&
                localDateTime.equals(picture.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, geometry, localDateTime);
    }
}
