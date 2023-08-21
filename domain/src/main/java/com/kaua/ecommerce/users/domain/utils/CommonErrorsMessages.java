package com.kaua.ecommerce.users.domain.utils;

public final class CommonErrorsMessages {

    private CommonErrorsMessages() {}

    public static String commonLengthErrorMessage(final String aField, final int aMinimumLength, final int aMaximumLength) {
        return "'" + aField + "'" + " must be between " + aMinimumLength + " and " + aMaximumLength + " characters";
    }

    public static String commonNullOrBlankErrorMessage(final String aField) {
        return "'" + aField + "'" + " should not be null or blank";
    }
}
