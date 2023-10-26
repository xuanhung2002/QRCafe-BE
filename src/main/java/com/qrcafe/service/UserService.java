package com.qrcafe.service;

import com.qrcafe.entity.User;

public interface UserService {
    boolean existedByUsername(String username);

    User save(User user);

    User getUserByUsername(String username);
}
