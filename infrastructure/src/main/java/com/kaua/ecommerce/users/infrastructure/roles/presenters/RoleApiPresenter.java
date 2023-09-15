package com.kaua.ecommerce.users.infrastructure.roles.presenters;

import com.kaua.ecommerce.users.application.role.retrieve.get.GetRoleByIdOutput;
import com.kaua.ecommerce.users.application.role.retrieve.list.ListRolesOutput;
import com.kaua.ecommerce.users.infrastructure.roles.models.GetRoleResponse;
import com.kaua.ecommerce.users.infrastructure.roles.models.ListRoleResponse;

public final class RoleApiPresenter {

    private RoleApiPresenter() {}

    public static GetRoleResponse present(final GetRoleByIdOutput aOutput) {
        return new GetRoleResponse(
                aOutput.id(),
                aOutput.name(),
                aOutput.description(),
                aOutput.roleType(),
                aOutput.createdAt(),
                aOutput.updatedAt()
        );
    }

    public static ListRoleResponse present(final ListRolesOutput aOutput) {
        return new ListRoleResponse(
                aOutput.id().getValue(),
                aOutput.name(),
                aOutput.description(),
                aOutput.type().name(),
                aOutput.createdAt().toString()
        );
    }
}
