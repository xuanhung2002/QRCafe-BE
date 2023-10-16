package com.qrcafe.initalization;

import com.qrcafe.entity.Role;
import com.qrcafe.entity.User;
import com.qrcafe.enums.RolesEnum;
import com.qrcafe.repository.RoleRepository;
import com.qrcafe.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Initializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Initializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
//        createRoleIfNotFound(RolesEnum.ADMIN);
//        createRoleIfNotFound(RolesEnum.CUSTOMER);
//        createRoleIfNotFound(RolesEnum.STAFF);
//        createRootUser();

        //Test
        User user = new User();
        user.setUsername("xuanhung");
        user.setPassword(passwordEncoder.encode("xuanhung"));
        user.setEmail("xuanhung09052002@gmail.com");
        userRepository.save(user);
    }

    private void createRoleIfNotFound(RolesEnum roleName) {
        System.out.println("hahahahaahaaa   " + roleRepository.findByName(roleName));
        if (roleRepository.findByName(roleName) == null) {
            Role role = new Role(roleName);
            roleRepository.save(role);
        }
    }
    private void createRootUser() {
        User user = new User();

        Role role = new Role(RolesEnum.ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        if(!userRepository.existsByUsername("admin")){
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin12345"));
            user.setEmail("admin12345@gmail.com");
            userRepository.save(user);
        }

    }
}