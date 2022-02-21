package com.oh29oh29.hellosecurity.service;

import com.oh29oh29.hellosecurity.domain.Account;
import com.oh29oh29.hellosecurity.domain.AccountDto;
import com.oh29oh29.hellosecurity.domain.Role;
import com.oh29oh29.hellosecurity.repository.RoleRepository;
import com.oh29oh29.hellosecurity.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(Account account) {
        final Role role = roleRepository.findByRoleName("ROLE_USER");
        final Set<Role> roles = new HashSet<>();
        roles.add(role);
        account.setUserRoles(roles);
        userRepository.save(account);
    }

    @Transactional
    public void modifyUser(AccountDto accountDto) {
        final ModelMapper modelMapper = new ModelMapper();
        final Account account = modelMapper.map(accountDto, Account.class);

        if (accountDto.getRoles() != null) {
            final Set<Role> roles = new HashSet<>();
            accountDto.getRoles().forEach(role -> {
                roles.add(roleRepository.findByRoleName(role));
            });
            account.setUserRoles(roles);
        }
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        userRepository.save(account);
    }

    @Transactional
    public AccountDto getUser(Long id) {
        final ModelMapper modelMapper = new ModelMapper();
        final Account account = userRepository.findById(id).orElse(new Account());
        final AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        final List<String> roles = account.getUserRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());

        accountDto.setRoles(roles);
        return accountDto;
    }

    @Transactional
    public List<Account> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
