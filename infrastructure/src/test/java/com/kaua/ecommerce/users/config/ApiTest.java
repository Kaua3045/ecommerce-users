package com.kaua.ecommerce.users.config;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public final class ApiTest {

    private ApiTest() {}

    public static JwtRequestPostProcessor ADMIN_JWT =
            jwt().jwt(jwt -> jwt
                            .claim("authorities", Set.of("list-accounts", "manage-permissions", "manage-roles"))
                            .claim("role", "ADMIN").build());
}
