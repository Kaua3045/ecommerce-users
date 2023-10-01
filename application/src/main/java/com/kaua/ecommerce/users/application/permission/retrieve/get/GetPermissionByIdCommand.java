package com.kaua.ecommerce.users.application.permission.retrieve.get;

public record GetPermissionByIdCommand(String id) {

    public static GetPermissionByIdCommand with(String id) {
        return new GetPermissionByIdCommand(id);
    }
}
