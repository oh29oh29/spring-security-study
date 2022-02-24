package com.oh29oh29.hellosecurity.service;

import com.oh29oh29.hellosecurity.domain.Resources;
import com.oh29oh29.hellosecurity.repository.ResourcesRepository;
import com.oh29oh29.hellosecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourcesService {

    private final ResourcesRepository resourcesRepository;
    private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

    public ResourcesService(ResourcesRepository resourcesRepository1, UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource) {
        this.resourcesRepository = resourcesRepository1;
        this.urlFilterInvocationSecurityMetadataSource = urlFilterInvocationSecurityMetadataSource;
    }

    @Transactional
    public Resources getResources(long id) {
        return resourcesRepository.findById(id).orElse(new Resources());
    }

    @Transactional
    public List<Resources> getResources() {
        return resourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Transactional
    public void createResources(Resources resources) throws Exception {
        resourcesRepository.save(resources);
        urlFilterInvocationSecurityMetadataSource.reload();
    }

    @Transactional
    public void deleteResources(long id) {
        resourcesRepository.deleteById(id);
    }
}