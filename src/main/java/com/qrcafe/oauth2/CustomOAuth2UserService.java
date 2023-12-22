package com.qrcafe.oauth2;

import com.qrcafe.entity.CustomUserDetails;
import com.qrcafe.entity.Role;
import com.qrcafe.entity.User;
import com.qrcafe.enums.RolesEnum;
import com.qrcafe.service.RoleService;
import com.qrcafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final RoleService roleService;
    private final UserService userService;
    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

    public CustomOAuth2UserService(RoleService roleService, UserService userService, List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors) {
        this.roleService = roleService;
        this.userService = userService;
        this.oAuth2UserInfoExtractors = oAuth2UserInfoExtractors;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional = oAuth2UserInfoExtractors.stream()
                .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
                .findFirst();
        if (oAuth2UserInfoExtractorOptional.isEmpty()) {
            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
        }

        CustomUserDetails customUserDetails = oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
        User user = upsertUser(customUserDetails);
        customUserDetails.setId(user.getId());
        return customUserDetails;
    }

    private User upsertUser(CustomUserDetails customUserDetails) {
        User user = userService.getUserByUsername(customUserDetails.getUsername());
        if (user == null) {
            user = new User();
            user.setUsername(customUserDetails.getUsername());
            user.setFullName(customUserDetails.getName());
            user.setEmail(customUserDetails.getEmail());
            user.setOAuth2Provider(customUserDetails.getProvider());
            Role role = roleService.getRoleByRoleName(RolesEnum.CUSTOMER);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        } else {
            user.setEmail(customUserDetails.getEmail());
        }
        return userService.save(user);
    }
}
