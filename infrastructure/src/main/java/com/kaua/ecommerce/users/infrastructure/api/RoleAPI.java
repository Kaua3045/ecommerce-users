package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.infrastructure.roles.models.CreateRoleApiInput;
import com.kaua.ecommerce.users.infrastructure.roles.models.GetRoleResponse;
import com.kaua.ecommerce.users.infrastructure.roles.models.ListRoleResponse;
import com.kaua.ecommerce.users.infrastructure.roles.models.UpdateRoleApiInput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "roles")
public interface RoleAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createRole(@RequestBody CreateRoleApiInput input);

    @PatchMapping("{id}")
    ResponseEntity<?> updateRole(
            @PathVariable String id,
            @RequestBody UpdateRoleApiInput input
    );

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    GetRoleResponse getRole(@PathVariable String id);

    @GetMapping
    Pagination<ListRoleResponse> listRoles(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteRole(@PathVariable String id);

    @DeleteMapping("{roleId}/permissions/{permissionId}")
    ResponseEntity<?> deleteRolePermission(@PathVariable String roleId, @PathVariable String permissionId);
}
