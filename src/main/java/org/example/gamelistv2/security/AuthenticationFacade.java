package org.example.gamelistv2.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {

    Authentication getAuthentication();

    String getUsername();
}
