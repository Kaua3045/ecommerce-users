package com.kaua.ecommerce.users.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "accounts/confirm")
public interface AccountMailAPI {

    @PatchMapping("{token}")
    ResponseEntity<?> confirmAccount(@PathVariable String token);

    @PostMapping("{accountId}")
    ResponseEntity<?> createAccountConfirmation(@PathVariable String accountId);
}
