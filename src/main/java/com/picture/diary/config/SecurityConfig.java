package com.picture.diary.config;

import com.picture.diary.common.filter.JsonUsernamePasswordAuthenticationFilter;
import com.picture.diary.common.user.data.Role;
import com.picture.diary.login.handler.LoginFailureHandler;
import com.picture.diary.login.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //private final JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    private final static String[] PERMIT_ALL_PATTERNS = {"/login", "/css/**" , "/images/**", "/js/**"};
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
                .logoutSuccessUrl("/login")
            .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(loginSuccessHandler)
                .defaultSuccessUrl("/map")
                .failureHandler(loginFailureHandler);

            //.and()
                //.addFilterBefore(jsonUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
