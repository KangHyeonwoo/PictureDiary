package com.picture.diary.common.user.repository;

import com.picture.diary.login.data.LoginType;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository {

    UserDetails findByUserId(String userId);

    UserDetails findByUserIdAndLoginType(String userId, LoginType loginType);
}
