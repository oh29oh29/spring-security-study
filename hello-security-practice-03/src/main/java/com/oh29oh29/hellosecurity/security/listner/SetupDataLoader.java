package com.oh29oh29.hellosecurity.security.listner;

import com.oh29oh29.hellosecurity.domain.Account;
import com.oh29oh29.hellosecurity.domain.Resources;
import com.oh29oh29.hellosecurity.domain.Role;
import com.oh29oh29.hellosecurity.repository.ResourcesRepository;
import com.oh29oh29.hellosecurity.repository.RoleRepository;
import com.oh29oh29.hellosecurity.repository.UserRepository;
import com.oh29oh29.hellosecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResourcesRepository resourcesRepository;
    private final PasswordEncoder passwordEncoder;
    private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

    public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, ResourcesRepository resourcesRepository, PasswordEncoder passwordEncoder, UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.resourcesRepository = resourcesRepository;
        this.passwordEncoder = passwordEncoder;
        this.urlFilterInvocationSecurityMetadataSource = urlFilterInvocationSecurityMetadataSource;
    }

    private static final AtomicInteger count = new AtomicInteger(0);

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        try {
            setupSecurityResources();
        } catch (Exception e) {
            e.printStackTrace();
        }

        alreadySetup = true;
    }



    private void setupSecurityResources() throws Exception {
        Set<Role> roles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "?????????");
        roles.add(adminRole);
        createResourceIfNotFound("/admin/**", "", roles, "url");
        urlFilterInvocationSecurityMetadataSource.reload();
        Account account = createUserIfNotFound("admin", "pass", "admin@gmail.com", 10,  roles);
        
//        Set<Role> roles1 = new HashSet<>();
//
//        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "?????????");
//        roles1.add(managerRole);
//        createResourceIfNotFound("io.security.corespringsecurity.aopsecurity.method.AopMethodService.methodTest", "", roles1, "method");
//        createResourceIfNotFound("io.security.corespringsecurity.aopsecurity.method.AopMethodService.innerCallMethodTest", "", roles1, "method");
//        createResourceIfNotFound("execution(* io.security.corespringsecurity.aopsecurity.pointcut.*Service.*(..))", "", roles1, "pointcut");
//        createUserIfNotFound("manager", "pass", "manager@gmail.com", 20, roles1);
//
//        Set<Role> roles3 = new HashSet<>();
//
//        Role childRole1 = createRoleIfNotFound("ROLE_USER", "??????");
//        roles3.add(childRole1);
//        createResourceIfNotFound("/users/**", "", roles3, "url");
//        createUserIfNotFound("user", "pass", "user@gmail.com", 30, roles3);

    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = new Role();
            role.setRoleName(roleName);
            role.setRoleDesc(roleDesc);
        }
        return roleRepository.save(role);
    }

    @Transactional
    public Account createUserIfNotFound(String userName, String password, String email, int age, Set<Role> roleSet) {
        Account account = userRepository.findByUsername(userName);

        if (account == null) {
            account = new Account();
            account.setUsername(userName);
            account.setPassword(passwordEncoder.encode(password));
            account.setEmail(email);
            account.setAge(age);
            account.setUserRoles(roleSet);
        }
        return userRepository.save(account);
    }

    @Transactional
    public Resources createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resources == null) {
            resources = new Resources();
            resources.setResourceName(resourceName);
            resources.setResourceType(resourceType);
            resources.setRoleSet(roleSet);
            resources.setHttpMethod(httpMethod);
            resources.setOrderNum(count.incrementAndGet());
        }
        return resourcesRepository.save(resources);
    }
}