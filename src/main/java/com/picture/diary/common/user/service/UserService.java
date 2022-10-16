package com.picture.diary.common.user.service;

import com.picture.diary.common.user.repository.UserRepository;
import com.picture.diary.login.data.LoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    //디폴트 로그인 타입 Synology_NAS
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserId(username);
    }

    public UserDetails loadUserByUsername(String username, LoginType loginType) throws UsernameNotFoundException {
        return userRepository.findByUserId(loginType, username);
    }

}
