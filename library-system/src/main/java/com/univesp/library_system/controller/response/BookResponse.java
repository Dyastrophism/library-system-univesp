package com.univesp.library_system.controller.response;

import lombok.Builder;

@Builder
public record BookResponse(
        Integer id,
        String title,
        String authorName,
        String isbn,
        String synopsis,
        String ownerName,
        byte[] cover,
        double rate,
        boolean archived,
        boolean shareable
) {
}
