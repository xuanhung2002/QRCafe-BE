package com.qrcafe.service.impl;


import com.qrcafe.dto.UserLocationDTO;
import com.qrcafe.entity.User;
import com.qrcafe.entity.UserLocation;
import com.qrcafe.repository.UserLocationRepository;
import com.qrcafe.repository.UserRepository;
import com.qrcafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;
  @Autowired
  UserLocationRepository userLocationRepository;

  @Override
  public boolean existedByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public List<UserLocation> getAllUserLocationsOfUser(String username) {
    return userLocationRepository.getUserLocationsByUser_Username(username);
  }

  @Override
  public UserLocation getUserLocationByIdLocation(Long id) {
    return userLocationRepository.getUserLocationById(id);
  }

  @Transactional
  @Override
  public boolean updateUserLocation(String username, UserLocationDTO userLocationDTO) {
    List<UserLocation> userLocations = getAllUserLocationsOfUser(username);
    Long targetUserLocationId = userLocationDTO.getId();

    UserLocation oldUserLocation = userLocations.stream()
            .filter(location -> Objects.equals(location.getId(), targetUserLocationId))
            .findFirst()
            .orElse(null);

    if (oldUserLocation != null) {
      oldUserLocation.setAddress(userLocationDTO.getAddress());
      oldUserLocation.setPhoneNumber(userLocationDTO.getPhoneNumber());
      oldUserLocation.setFullName(userLocationDTO.getFullName());

      try {
        userLocationRepository.save(oldUserLocation);
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }

  @Override
  public UserLocation addUserLocation(String username, UserLocationDTO userLocationDTO) {
    User user = getUserByUsername(username);
    if(user != null){
        UserLocation userLocation = UserLocation.builder()
                .user(user)
                .fullName(userLocationDTO.getFullName())
                .phoneNumber(userLocationDTO.getPhoneNumber())
                .address(userLocationDTO.getAddress())
                .build();
        try{
          return userLocationRepository.save(userLocation);
        }catch (Exception e){
          e.printStackTrace();
          return null;
        }
    }else {
      return null;
    }
  }
}
