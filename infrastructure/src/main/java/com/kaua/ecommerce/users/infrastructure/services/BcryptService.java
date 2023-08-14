package com.kaua.ecommerce.users.infrastructure.services;

import com.kaua.ecommerce.users.application.gateways.EncrypterGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptService implements EncrypterGateway {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BcryptService(final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public String encrypt(String rawPassword) {
        return this.bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encryptedPassword) {
        return this.bCryptPasswordEncoder.matches(rawPassword, encryptedPassword);
    }
}
