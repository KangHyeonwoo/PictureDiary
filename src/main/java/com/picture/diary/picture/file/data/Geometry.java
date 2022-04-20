package com.picture.diary.picture.file.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;


/**
 * 좌표 VO
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Geometry {

    private double latitude;

    private double longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Geometry geometry = (Geometry) o;

        return Double.compare(geometry.latitude, latitude) == 0 && Double.compare(geometry.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
