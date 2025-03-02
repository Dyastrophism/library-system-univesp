package com.univesp.library_system.service;

import com.univesp.library_system.controller.request.FeedbackRequest;
import com.univesp.library_system.controller.response.FeedbackResponse;
import com.univesp.library_system.controller.response.PageResponse;
import com.univesp.library_system.domain.Book;
import com.univesp.library_system.domain.Feedback;
import com.univesp.library_system.domain.User;
import com.univesp.library_system.handler.OperationNotPermittedException;
import com.univesp.library_system.repository.BookRepository;
import com.univesp.library_system.repository.FeedbackRepository;
import com.univesp.library_system.service.mapper.FeedbackMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    /**
     * Save feedback
     * @param feedbackRequest feedback request
     * @param authentication authentication
     * @return feedback id
     */
    public Integer saveFeedback(FeedbackRequest feedbackRequest, Authentication authentication) {
        Book book = bookRepository.findById(feedbackRequest.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID:: " + feedbackRequest.bookId()));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("Book is not available for feedback");
        }
        User user = (User) authentication.getPrincipal();
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't give feedback to your own book");
        }
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        return feedbackRepository.save(feedback).getId();
    }

    /**
     * Find all feedbacks by book
     * @param bookId book id
     * @param page page
     * @param size size
     * @param authentication authentication
     * @return page response of feedbacks
     */
    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, Integer page, Integer size, Authentication authentication) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) authentication.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
