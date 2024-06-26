package org.example.gamelistv2.security;


import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.example.gamelistv2.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@EqualsAndHashCode
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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

    /**
     * When a user registers, a code is sent to his mail, the same
     * code is recorded in the database. When the user clicks on
     * the link in the mail, the code will be deleted from the
     * database, so the field will be null. Then the user will be
     * able to successfully log in
     */
    @Override
    public boolean isEnabled() {
        return user.getActivationCode() == null;
    }
}

