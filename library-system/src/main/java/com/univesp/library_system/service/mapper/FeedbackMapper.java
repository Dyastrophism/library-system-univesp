package com.univesp.library_system.service.mapper;


import com.univesp.library_system.controller.request.FeedbackRequest;
import com.univesp.library_system.controller.response.FeedbackResponse;
import com.univesp.library_system.domain.Book;
import com.univesp.library_system.domain.Feedback;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {

    public Feedback toFeedback(FeedbackRequest feedbackRequest) {
        return Feedback.builder()
                .note(feedbackRequest.note())
                .comment(feedbackRequest.comment())
                .book(Book.builder()
                        .id(feedbackRequest.bookId())
                        .archived(false) // Not required and has no impact :: just to justify lombok
                        .shareable(false) // Not required and has no impact :: just to justify lombok
                        .build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer userId) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), userId))
                .build();
    }
}
