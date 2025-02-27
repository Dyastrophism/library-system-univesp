package com.univesp.library_system.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token
) {
}
