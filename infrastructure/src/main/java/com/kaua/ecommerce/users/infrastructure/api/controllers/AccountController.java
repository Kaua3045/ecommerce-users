package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.infrastructure.api.AccountAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements AccountAPI {
    @Override
    public ResponseEntity<?> createAccount() {
        return null;
    }
}
