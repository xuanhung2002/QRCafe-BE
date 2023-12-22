//package com.qrcafe.entity;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//@Data
//public class CustomUserDetails implements UserDetails {
//    /**
//	 *
//	 */
//
//	private static final long serialVersionUID = 1L;
//
//
//	private User user;
//
//  @Override
//  public Collection<? extends GrantedAuthority> getAuthorities() {
//    Set<Role> roles = user.getRoles();
//    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//    for (Role role : roles) {
//      authorities.add(new SimpleGrantedAuthority(role.getName().name()));
//    }
//    return authorities;
//  }
//
//
//	public CustomUserDetails(User user) {
//		this.user = user;
//	}
//
//  public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	@Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUsername();
//    }
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
//////////////////////////////////////////////////////

package com.qrcafe.entity;

import com.qrcafe.oauth2.OAuth2Provider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class CustomUserDetails implements OAuth2User, UserDetails {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private OAuth2Provider provider;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return fullName;
    }
}


