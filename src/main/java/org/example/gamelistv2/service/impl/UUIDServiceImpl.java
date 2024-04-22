package org.example.gamelistv2.service.impl;


import org.example.gamelistv2.service.UUIDService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDServiceImpl implements UUIDService {

    public String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
