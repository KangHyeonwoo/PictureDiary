package com.picture.diary.config;

import com.picture.diary.login.data.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Reference : https://devuna.tistory.com/59
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationProvider authenticationProvider;

    private final static String[] PERMIT_ALL_PATTERNS = {"/", "/css/**" , "/images/**", "/js/**"};
    private final static String[] USER_ROLE_PATTERNS = {"/map"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
            .and()
                .authorizeRequests()
                .antMatchers(PERMIT_ALL_PATTERNS).permitAll()
                .antMatchers(USER_ROLE_PATTERNS).hasRole(Role.USER.name())
                .anyRequest().authenticated()
            .and()
                .logout()
                .logoutSuccessUrl("/")
            .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/map");
            //TODO OAUTH 로그인 기능 구현 시 주석 풀기
            //.and()
                //.oauth2Login()
                //.userInfoEndpoint();
                //.userService(customOAuthUserService);
    }
}
