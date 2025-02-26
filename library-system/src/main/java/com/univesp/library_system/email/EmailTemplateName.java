package com.univesp.library_system.email;

import lombok.Getter;

public enum EmailTemplateName {

    ACTIVATION_ACCOUNT("activation_account");

    @Getter
    private final String templateName;

    EmailTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
