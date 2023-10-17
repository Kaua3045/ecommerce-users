package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.GetAccountPresenter;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ListAccountsPresenter;
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

    @GetMapping
    Pagination<ListAccountsPresenter> listAccounts(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @PatchMapping("{id}/avatar")
    ResponseEntity<?> updateAccountAvatar(
            @PathVariable String id,
            @RequestParam("avatar") MultipartFile avatarFile
    );

    @PatchMapping("{id}/role")
    ResponseEntity<?> updateAccountRole(
            @PathVariable String id,
            @RequestBody UpdateAccountRoleApiInput input
    );

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteAccount(@PathVariable String id);
}
