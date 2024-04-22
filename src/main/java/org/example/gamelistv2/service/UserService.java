package org.example.gamelistv2.service;

import org.example.gamelistv2.entity.User;
import org.example.gamelistv2.view.UserView;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User find(String username);

    void save(UserView user);

    boolean activateUser(String code);

    void uploadProfilePicture(byte[] bytes);

    byte[] getProfilePicture();
}
