package com.picture.diary.common.user.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {

    private String userId;

    private String username;

    private Role role;
}
