package com.kaua.ecommerce.users.application.gateways;

public interface EncrypterGateway {

    String encrypt(String rawPassword);

    boolean matches(String rawPassword, String encryptedPassword);
}
