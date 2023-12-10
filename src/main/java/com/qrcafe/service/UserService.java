package com.qrcafe.service;

import com.qrcafe.dto.RegisterDTO;
import com.qrcafe.dto.UserLocationDTO;
import com.qrcafe.entity.Role;
import com.qrcafe.entity.User;
import com.qrcafe.entity.UserLocation;

import java.util.List;

public interface UserService {
    boolean existedByUsername(String username);

    User save(User user);

    User getUserByUsername(String username);

    List<UserLocation> getAllUserLocationsOfUser(String username);

    UserLocation getUserLocationByIdLocation(Long id);

    boolean updateUserLocation(String username, UserLocationDTO userLocationDTO);

    UserLocation addUserLocation(String username, UserLocationDTO userLocationDTO);

    boolean grantPermissionForStaff(String username, String role);
    boolean deletePermissionOfStaff(String username);

    void resetUserPassword(String username);

    List<User> getAllStaff();

    boolean createAccountForStaff(RegisterDTO registerDTO);
    boolean deleteUserAccount(String username);

    List<User> getAllUsers();

    List<Role> getRoleByUserName(String username);
}
