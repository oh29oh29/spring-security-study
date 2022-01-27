package com.oh29oh29.hellosecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class SecurityController {

    @GetMapping("/")
    public String index(HttpSession session) {
        final Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();

        final SecurityContext context = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        final Authentication authentication2 = context.getAuthentication();

        return "home";
    }

    @GetMapping("/thread")
    public String thread() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    }
                }
        ).start();

        return "thread";
    }
}
