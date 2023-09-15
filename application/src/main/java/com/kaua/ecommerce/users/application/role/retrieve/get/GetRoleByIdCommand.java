package com.kaua.ecommerce.users.application.role.retrieve.get;

public record GetRoleByIdCommand(String id) {

    public static GetRoleByIdCommand with(String id) {
        return new GetRoleByIdCommand(id);
    }
}
