package com.picture.diary.config;

import com.picture.diary.login.data.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //static 디렉토리 하위 파일 목록은 인증 무시( = 항상 통과)
        web.ignoring().antMatchers(
                "/css/**", "/js/**", "/img/**", "/lib/**", "/less/**", "/assets/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {

    }
    */

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
                .oauth2Login()
                .userInfoEndpoint();
                //.userService(customOAuthUserService);
    }
}
