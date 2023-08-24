package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.infrastructure.accounts.models.RequestResetPasswordApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ResetPasswordApiInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "accounts")
public interface AccountMailAPI {

    @PatchMapping("/confirm/{token}")
    ResponseEntity<?> confirmAccount(@PathVariable String token);

    @PostMapping("/confirm/{accountId}")
    ResponseEntity<?> createAccountConfirmation(@PathVariable String accountId);

    @PostMapping("/request-reset-password")
    ResponseEntity<?> requestResetPassword(@RequestBody RequestResetPasswordApiInput input);

    @PatchMapping("/reset-password/{token}")
    ResponseEntity<?> resetPassword(@RequestBody ResetPasswordApiInput input, @PathVariable String token);
}
