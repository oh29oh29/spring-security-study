package com.oh29oh29.hellosecurity.security.config;

import com.oh29oh29.hellosecurity.security.common.FormAuthenticationDetailsSource;
import com.oh29oh29.hellosecurity.security.handler.CustomAccessDeniedHandler;
import com.oh29oh29.hellosecurity.security.handler.CustomAuthenticationFailureHandler;
import com.oh29oh29.hellosecurity.security.handler.CustomAuthenticationSuccessHandler;
import com.oh29oh29.hellosecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import com.oh29oh29.hellosecurity.security.provider.CustomAuthenticationProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final FormAuthenticationDetailsSource authenticationDetailsSource;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

    public SecurityConfig(UserDetailsService userDetailsService, FormAuthenticationDetailsSource authenticationDetailsSource, CustomAuthenticationSuccessHandler authenticationSuccessHandler, CustomAuthenticationFailureHandler authenticationFailureHandler, UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource) {
        this.userDetailsService = userDetailsService;
        this.authenticationDetailsSource = authenticationDetailsSource;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.urlFilterInvocationSecurityMetadataSource = urlFilterInvocationSecurityMetadataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider(userDetailsService, passwordEncoder()));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/h2-console/**", "/users", "/login*").permitAll()
                .anyRequest().authenticated();

        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .defaultSuccessUrl("/")
                .authenticationDetailsSource(authenticationDetailsSource)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll();

        http
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());


        // h2-console 접속을 위한 설정
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler("/denied");
    }

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        return filterSecurityInterceptor;
    }

    @Bean
    public AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        return Collections.singletonList(new RoleVoter());
    }

}