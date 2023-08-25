package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.application.account.retrieve.get.GetAccountOutput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
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

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<GetAccountOutput> getAccount(@PathVariable String id);

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteAccount(@PathVariable String id);
}
