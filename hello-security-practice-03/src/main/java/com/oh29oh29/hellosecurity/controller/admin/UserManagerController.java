package com.oh29oh29.hellosecurity.controller.admin;

import com.oh29oh29.hellosecurity.domain.Account;
import com.oh29oh29.hellosecurity.domain.AccountDto;
import com.oh29oh29.hellosecurity.domain.Role;
import com.oh29oh29.hellosecurity.service.RoleService;
import com.oh29oh29.hellosecurity.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserManagerController {

    private final UserService userService;
    private final RoleService roleService;

    public UserManagerController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin/accounts")
    public String getUsers(Model model) throws Exception {
        final List<Account> accounts = userService.getUsers();
        model.addAttribute("accounts", accounts);

        return "admin/user/list";
    }

    @PostMapping("/admin/accounts")
    public String modifyUser(AccountDto accountDto) throws Exception {
        userService.modifyUser(accountDto);

        return "redirect:/admin/accounts";
    }

    @GetMapping("/admin/accounts/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        final AccountDto accountDto = userService.getUser(id);
        final List<Role> roleList = roleService.getRoles();

        model.addAttribute("account", accountDto);
        model.addAttribute("roleList", roleList);

        return "admin/user/detail";
    }

    @GetMapping("/admin/accounts/delete/{id}")
    public String removeUser(@PathVariable("id") Long id, Model model) {
        userService.deleteUser(id);

        return "redirect:/admin/users";
    }
}
