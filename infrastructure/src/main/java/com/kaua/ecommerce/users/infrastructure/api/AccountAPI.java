package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.GetAccountPresenter;
import com.kaua.ecommerce.users.infrastructure.accounts.models.UpdateAccountRoleApiInput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(value = "accounts")
public interface AccountAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createAccount(@RequestBody CreateAccountApiInput input);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    GetAccountPresenter getAccount(@PathVariable String id);

    @PatchMapping("{id}")
    ResponseEntity<?> updateAccountAvatar(
            @PathVariable String id,
            @RequestParam("avatar") MultipartFile avatarFile
    );

    @PatchMapping("{id}")
    ResponseEntity<?> updateAccountRole(
            @PathVariable String id,
            @RequestBody UpdateAccountRoleApiInput input
    );

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteAccount(@PathVariable String id);
}
