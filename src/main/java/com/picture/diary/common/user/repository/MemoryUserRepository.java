package com.picture.diary.common.user.repository;

import com.picture.diary.common.user.data.Role;
import com.picture.diary.common.user.data.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemoryUserRepository implements UserRepository {
    private Map<String, UserDetails> userMap = new HashMap<>();

    @PostConstruct
    public void init() {
        UserDetails hwkang = new User("hwkang", "username", Role.USER);
        userMap.put("hwkang", hwkang);
    }


    @Override
    public UserDetails findByUserId(String userId) {
        return userMap.get(userId);
    }
}
