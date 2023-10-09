package com.kaua.ecommerce.users.infrastructure.service.impl;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.infrastructure.services.impl.BcryptService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@IntegrationTest
public class BcryptServiceTest {

    @Autowired
    private BcryptService bcryptService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void givenAValidRawPasswordAndEncodedPassword_whenCallMatches_shouldReturnTrue() {
        final var rawPassword = "1234567Ab";
        final var encodedPassword = bCryptPasswordEncoder.encode(rawPassword);

        final var actual = bcryptService.matches(rawPassword, encodedPassword);

        Assertions.assertTrue(actual);
    }

    @Test
    void givenAValidRawPassword_whenCallEncrypt_shouldReturnPasswordEncrypted() {
        final var rawPassword = "1234567Ab";

        final var actual = bcryptService.encrypt(rawPassword);

        Assertions.assertNotNull(actual);
    }

    @Test
    void givenAnInvalidRawPasswordAndValidEncodedPassword_whenCallMatches_shouldReturnFalse() {
        final var rawPassword = "1234567Ab";
        final var encodedPassword = bCryptPasswordEncoder.encode("1234567Abc");

        final var actual = bcryptService.matches(rawPassword, encodedPassword);

        Assertions.assertFalse(actual);
    }
}
