package com.oh29oh29.hellosecurity.security.config;

import com.oh29oh29.hellosecurity.security.common.AjaxLoginAuthenticationEntryPoint;
import com.oh29oh29.hellosecurity.security.filter.AjaxAuthenticationFilter;
import com.oh29oh29.hellosecurity.security.handler.AjaxAccessDeniedHandler;
import com.oh29oh29.hellosecurity.security.handler.AjaxAuthenticationFailureHandler;
import com.oh29oh29.hellosecurity.security.handler.AjaxAuthenticationSuccessHandler;
import com.oh29oh29.hellosecurity.security.provider.AjaxAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(0)
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AjaxSecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthenticationProvider(userDetailsService, passwordEncoder));
    }

    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new AjaxAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/messages").hasRole("MANAGER")
                .anyRequest().authenticated();

        http
                .addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .exceptionHandling()
                .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
                .accessDeniedHandler(ajaxAccessDeniedHandler());

        http.csrf().disable();
    }

    @Bean
    public AccessDeniedHandler ajaxAccessDeniedHandler() {
        return new AjaxAccessDeniedHandler();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AjaxAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
        final AjaxAuthenticationFilter ajaxLoginProcessingFilter = new AjaxAuthenticationFilter("/api/login");
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
        return ajaxLoginProcessingFilter;
    }

    @Bean
    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }
}
