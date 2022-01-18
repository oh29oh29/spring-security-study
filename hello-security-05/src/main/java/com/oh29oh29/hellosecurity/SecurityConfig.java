package com.oh29oh29.hellosecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated();

        http
                .formLogin();

        http
                .sessionManagement()
                .maximumSessions(1)
                /*
                * false: maximumSessions 값이 넘어가면 기존 사용자 세션 만료 (default)
                * true: maximumSessions 값이 넘어가면 신규 사용자 로그인 차단
                * */
                .maxSessionsPreventsLogin(false);
    }
}
