package com.picture.diary.common.user.repository;

import com.picture.diary.common.user.data.Role;
import com.picture.diary.common.user.data.User;
import com.picture.diary.login.data.LoginType;
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
        UserDetails hwkang = new User("hwkang", "username", Role.USER, LoginType.SYNOLOGY_NAS);
        userMap.put("hwkang", hwkang);
    }


    @Override
    public UserDetails findByUserId(String userId) {
        return userMap.get(userId);
    }

    @Override
    public UserDetails findByUserIdAndLoginType(String userId, LoginType loginType) {
        userMap.keySet().stream()
                .filter(key -> key.equals(userId) && userMap.get(key).get)
    }
}
