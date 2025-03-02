package com.univesp.library_system.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED, "No code defined"),
    ACCOUNT_LOCKED(300, BAD_REQUEST, "Account locked"),
    NEW_PASSWORD_DOES_NOT_MATCH(300, BAD_REQUEST, "New password does not match"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Incorrect current password"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "Account disabled"),
    BAD_CREDENTIALS(304, UNAUTHORIZED, "Bad credentials"),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final  HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
