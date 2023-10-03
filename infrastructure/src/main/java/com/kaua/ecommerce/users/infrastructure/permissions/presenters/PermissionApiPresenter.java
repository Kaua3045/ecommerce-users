package com.kaua.ecommerce.users.infrastructure.permissions.presenters;

import com.kaua.ecommerce.users.application.permission.retrieve.get.GetPermissionByIdOutput;
import com.kaua.ecommerce.users.infrastructure.permissions.models.GetPermissionResponse;

public final class PermissionApiPresenter {

    private PermissionApiPresenter() {}

    public static GetPermissionResponse present(final GetPermissionByIdOutput aOutput) {
        return new GetPermissionResponse(
                aOutput.id(),
                aOutput.name(),
                aOutput.description(),
                aOutput.createdAt(),
                aOutput.updatedAt()
        );
    }
}
