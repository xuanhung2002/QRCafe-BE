package com.qrcafe.service;

import com.qrcafe.dto.UserLocationDTO;
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
}
