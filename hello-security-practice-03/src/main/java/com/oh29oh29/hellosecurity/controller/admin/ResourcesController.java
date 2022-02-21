package com.oh29oh29.hellosecurity.controller.admin;

import com.oh29oh29.hellosecurity.domain.Resources;
import com.oh29oh29.hellosecurity.domain.ResourcesDto;
import com.oh29oh29.hellosecurity.domain.Role;
import com.oh29oh29.hellosecurity.repository.RoleRepository;
import com.oh29oh29.hellosecurity.service.ResourcesService;
import com.oh29oh29.hellosecurity.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ResourcesController {

    private final ResourcesService resourcesService;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public ResourcesController(ResourcesService resourcesService, RoleRepository roleRepository, RoleService roleService) {
        this.resourcesService = resourcesService;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    @GetMapping("/admin/resources")
    public String getResources(Model model) throws Exception {
        final List<Resources> resources = resourcesService.getResources();
        model.addAttribute("resources", resources);

        return "admin/resource/list";
    }

    @PostMapping("/admin/resources")
    public String createResources(ResourcesDto resourcesDto) throws Exception {
        final ModelMapper modelMapper = new ModelMapper();
        final Role role = roleRepository.findByRoleName(resourcesDto.getRoleName());
        final Set<Role> roles = new HashSet<>();
        roles.add(role);
        final Resources resources = modelMapper.map(resourcesDto, Resources.class);
        resources.setRoleSet(roles);

        resourcesService.createResources(resources);

        return "redirect:/admin/resources";
    }

    @GetMapping("/admin/resources/register")
    public String viewRoles(Model model) throws Exception {
        final List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        final ResourcesDto resources = new ResourcesDto();
        final Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        resources.setRoleSet(roleSet);
        model.addAttribute("resources", resources);

        return "admin/resource/detail";
    }

    @GetMapping("/admin/resources/{id}")
    public String getResources(@PathVariable String id, Model model) throws Exception {
        final ModelMapper modelMapper = new ModelMapper();

        final List<Role> roles = roleService.getRoles();
        model.addAttribute("roleList", roles);

        final Resources resources = resourcesService.getResources(Long.parseLong(id));
        final ResourcesDto resourcesDto = modelMapper.map(resources, ResourcesDto.class);
        model.addAttribute("resources", resourcesDto);

        return "admin/resource/detail";
    }

    @GetMapping("/admin/resources/delete/{id}")
    public String removeResources(@PathVariable String id, Model model) throws Exception {
        resourcesService.deleteResources(Long.parseLong(id));

        return "redirect:/admin/resources";
    }
}
