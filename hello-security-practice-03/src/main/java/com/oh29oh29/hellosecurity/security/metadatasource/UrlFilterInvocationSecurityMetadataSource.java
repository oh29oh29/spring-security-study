package com.oh29oh29.hellosecurity.security.metadatasource;

import com.oh29oh29.hellosecurity.security.factory.UrlResourcesMapFactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final UrlResourcesMapFactoryBean urlResourcesMapFactoryBean;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;

    public UrlFilterInvocationSecurityMetadataSource(UrlResourcesMapFactoryBean urlResourcesMapFactoryBean) throws Exception {
        this.urlResourcesMapFactoryBean = urlResourcesMapFactoryBean;
        this.requestMap = this.urlResourcesMapFactoryBean.getObject();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        HttpServletRequest request = ((FilterInvocation) object).getRequest();

        if (requestMap != null) {
            for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
                RequestMatcher matcher = entry.getKey();
                if (matcher.matches(request)) {
                    return entry.getValue();
                }
            }
        }

        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();

        for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap
                .entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    public void reload() throws Exception {
        requestMap = urlResourcesMapFactoryBean.getObject();
    }
}
