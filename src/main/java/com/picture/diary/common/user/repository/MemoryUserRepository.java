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
        UserDetails user1 = new User("user1", "username", Role.USER);
        userMap.put("user1", user1);
    }


    @Override
    public UserDetails findByUserId(String userId) {
        return userMap.get(userId);
    }
}
