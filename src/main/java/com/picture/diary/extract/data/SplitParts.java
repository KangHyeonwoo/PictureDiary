package com.picture.diary.extract.data;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum SplitParts {
    DOT("[.]"),
    SLASH("[/]");

    private String value;
}
