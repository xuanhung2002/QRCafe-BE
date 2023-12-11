package com.qrcafe.initalization;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.qrcafe.entity.Role;
import com.qrcafe.entity.User;
import com.qrcafe.enums.RolesEnum;
import com.qrcafe.repository.RoleRepository;
import com.qrcafe.repository.UserRepository;

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
        createRoleIfNotFound(RolesEnum.ADMIN);
        createRoleIfNotFound(RolesEnum.CUSTOMER);
        createRoleIfNotFound(RolesEnum.STAFF);
        createRootUser();
    }

    private void createRoleIfNotFound(RolesEnum roleName) {
        if (roleRepository.findByName(roleName) == null) {
            Role role = new Role(roleName);
            roleRepository.save(role);
        }
    }
    private void createRootUser() {
        // Check if the role exists, create it if not
        createRoleIfNotFound(RolesEnum.ADMIN);
        // Create a user and associate the role with it
        User checkedUser = userRepository.findByUsername("admin");
        if(checkedUser != null){
            checkedUser.setPassword(passwordEncoder.encode("admin12345"));
            userRepository.save(checkedUser);
        }
        else {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin12345"));
            user.setEmail("admin12345@gmail.com");
            Role role = roleRepository.findByName(RolesEnum.ADMIN);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            // Save the role first, then save the user
            roleRepository.save(role);
            userRepository.save(user);
        }

    }
}
