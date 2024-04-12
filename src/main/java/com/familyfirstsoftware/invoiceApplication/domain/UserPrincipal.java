package com.familyfirstsoftware.invoiceApplication.domain;


import com.familyfirstsoftware.invoiceApplication.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


import static com.familyfirstsoftware.invoiceApplication.dtoMapper.UserDTOMapper.fromUser;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final User user;
    private final Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // list in the db is comma separated so these two are the same
        //return stream(role.getPermission().split(",".trim())).map(SimpleGrantedAuthority::new).collect(toList());
        return AuthorityUtils.commaSeparatedStringToAuthorityList(role.getPermission()); // "USER:READ,USER:DELETE"
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // TODO implement the following method
    // create another table to save the password of the user with an expiration after 30 days
    // pass private final Date passwordExpirationDate;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return user.isNotLocked();
    }

    // TODO implement the following method
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public UserDTO getUser() {
        return fromUser(user, role);
    }
}
