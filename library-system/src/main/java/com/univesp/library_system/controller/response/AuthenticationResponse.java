package com.univesp.library_system.controller.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token
) {
}
