package com.kaua.ecommerce.users.infrastructure.roles.presenters;

import com.kaua.ecommerce.users.application.role.retrieve.get.GetRoleByIdOutput;
import com.kaua.ecommerce.users.infrastructure.roles.models.GetRoleOutput;

public final class RoleApiPresenter {

    private RoleApiPresenter() {}

    public static GetRoleOutput present(final GetRoleByIdOutput aOutput) {
        return new GetRoleOutput(
                aOutput.id(),
                aOutput.name(),
                aOutput.description(),
                aOutput.roleType(),
                aOutput.createdAt(),
                aOutput.updatedAt()
        );
    }
}
