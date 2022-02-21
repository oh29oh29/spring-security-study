package com.oh29oh29.hellosecurity.controller.admin;

import com.oh29oh29.hellosecurity.domain.Role;
import com.oh29oh29.hellosecurity.domain.RoleDto;
import com.oh29oh29.hellosecurity.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/admin/roles")
    public String getRoles(Model model) throws Exception {
        final List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);

        return "admin/role/list";
    }

    @GetMapping("/admin/roles/register")
    public String viewRoles(Model model) throws Exception {
        final RoleDto role = new RoleDto();
        model.addAttribute("role", role);

        return "admin/role/detail";
    }

    @PostMapping("/admin/roles")
    public String createRole(RoleDto roleDto) throws Exception {
        final ModelMapper modelMapper = new ModelMapper();

        final Role role = modelMapper.map(roleDto, Role.class);
        roleService.createRole(role);

        return "redirect:/admin/roles";
    }

    @GetMapping("/admin/roles/{id}")
    public String getRole(@PathVariable String id, Model model) throws Exception {
        final ModelMapper modelMapper = new ModelMapper();

        final Role role = roleService.getRole(Long.parseLong(id));
        final RoleDto roleDto = modelMapper.map(role, RoleDto.class);
        model.addAttribute("role", roleDto);

        return "admin/role/detail";
    }

    @GetMapping("/admin/roles/delete/{id}")
    public String removeResources(@PathVariable String id, Model model) throws Exception {
        roleService.deleteRole(Long.parseLong(id));

        return "redirect:/admin/resources";
    }
}
