package com.oh29oh29.hellosecurity.service;

import com.oh29oh29.hellosecurity.domain.Resources;
import com.oh29oh29.hellosecurity.repository.ResourcesRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourcesService {

    private final ResourcesRepository resourcesRepository;

    public ResourcesService(ResourcesRepository resourcesRepository1) {
        this.resourcesRepository = resourcesRepository1;
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
    public void createResources(Resources resources){
        resourcesRepository.save(resources);
    }

    @Transactional
    public void deleteResources(long id) {
        resourcesRepository.deleteById(id);
    }
}