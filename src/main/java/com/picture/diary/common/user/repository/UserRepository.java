package com.picture.diary.common.user.repository;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository {

    UserDetails findByUserId(String userId);
}
