package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.utils.Resource;

public interface AvatarGateway {

    String save(String aAccountId, Resource aResource);

    void delete(String accountId);
}
