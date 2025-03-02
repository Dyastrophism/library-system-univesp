package com.univesp.library_system.controller.response;

import lombok.Builder;

@Builder
public record FeedbackResponse(
        Double note,
        String comment,
        boolean ownFeedback
) {
}
