package org.example.gamelistv2.config;


import lombok.RequiredArgsConstructor;
import org.example.gamelistv2.GameListv2Application;
import org.example.gamelistv2.security.AuthenticationFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@EnableEnversRepositories(basePackageClasses = GameListv2Application.class)
@RequiredArgsConstructor
@Configuration
public class AuditConfig {

    private final AuthenticationFacade authenticationFacade;

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(authenticationFacade.getUsername());
    }
}
