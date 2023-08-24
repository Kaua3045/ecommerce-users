package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.RequestResetPasswordApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ResetPasswordApiInput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "accounts")
public interface AccountAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createAccount(@RequestBody CreateAccountApiInput input);

    @PostMapping("/request-reset-password")
    ResponseEntity<?> requestResetPassword(@RequestBody RequestResetPasswordApiInput input);

    @PatchMapping("/reset-password/{token}")
    ResponseEntity<?> resetPassword(@RequestBody ResetPasswordApiInput input, @PathVariable String token);
}
