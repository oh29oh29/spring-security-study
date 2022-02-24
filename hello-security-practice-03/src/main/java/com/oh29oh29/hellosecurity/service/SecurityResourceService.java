package com.oh29oh29.hellosecurity.service;

import com.oh29oh29.hellosecurity.domain.Resources;
import com.oh29oh29.hellosecurity.repository.ResourcesRepository;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class SecurityResourceService {

    private final ResourcesRepository resourcesRepository;

    public SecurityResourceService(ResourcesRepository resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResources() {
        final LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        final List<Resources> resources = resourcesRepository.findAllResources();

        resources.forEach(resource -> {
            final List<ConfigAttribute> configAttributes = new ArrayList<>();
            resource.getRoleSet().forEach(role -> {
                configAttributes.add(new SecurityConfig(role.getRoleName()));
                result.put(new AntPathRequestMatcher(resource.getResourceName()), configAttributes);
            });
        });

        return result;
    }

}
