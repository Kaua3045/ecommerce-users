package com.kaua.ecommerce.users.infrastructure.permissions.presenters;

import com.kaua.ecommerce.users.application.permission.retrieve.get.GetPermissionByIdOutput;
import com.kaua.ecommerce.users.application.permission.retrieve.list.ListPermissionsOutput;
import com.kaua.ecommerce.users.infrastructure.permissions.models.GetPermissionResponse;
import com.kaua.ecommerce.users.infrastructure.permissions.models.ListPermissionResponse;

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

    public static ListPermissionResponse present(final ListPermissionsOutput aOutput) {
        return new ListPermissionResponse(
                aOutput.id().getValue(),
                aOutput.name(),
                aOutput.description(),
                aOutput.createdAt().toString()
        );
    }
}
