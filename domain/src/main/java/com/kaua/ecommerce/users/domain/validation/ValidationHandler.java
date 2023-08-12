package com.kaua.ecommerce.users.domain.validation;

import java.util.List;

public interface ValidationHandler {

    // Interface fluente, retorna uma instância de ValidationHandler e podemos encadear os métodos
    ValidationHandler append(Error aError);

    ValidationHandler append(ValidationHandler aHandler);

    ValidationHandler validate(Validation aValidation);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !(getErrors().isEmpty());
    }
}
