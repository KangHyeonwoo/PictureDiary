package com.picture.diary.common.user.repository;

import com.picture.diary.common.user.data.Role;
import com.picture.diary.common.user.data.User;
import com.picture.diary.login.data.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MemoryUserRepository implements UserRepository {

    private Map<String, UserDetails> userMap = new HashMap<>();

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        UserDetails hwkang = new User("hwkang", passwordEncoder.encode("aaaa11!!"),"username", Role.USER, LoginType.SYNOLOGY_NAS);
        userMap.put("hwkang", hwkang);
    }


    @Override
    public UserDetails findByUserId(String userId) {
        return userMap.get(userId);
    }

    @Override
    public UserDetails findByUserIdAndLoginType(String userId, LoginType loginType) {
        userMap.keySet().stream()
                .filter(key -> key.equals(userId) && !userMap.get(key).getAuthorities().isEmpty())
                .map(key -> userMap.get(key).getAuthorities())
                .filter(auth -> auth.equals(LoginType.SYNOLOGY_NAS))
                .findAny().get();

        return null;
    }
}
