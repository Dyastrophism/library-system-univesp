package com.univesp.library_system.service.mapper;

import com.univesp.library_system.controller.request.BookRequest;
import com.univesp.library_system.controller.response.BookResponse;
import com.univesp.library_system.controller.response.BorrowedBookResponse;
import com.univesp.library_system.domain.Book;
import com.univesp.library_system.domain.BookTransactionHistory;
import org.springframework.stereotype.Service;

import static com.univesp.library_system.service.serviceimpl.FileUtils.readFileFromLocation;

@Service
public class BookMapper {

    public Book toBook(BookRequest bookRequest) {
        return Book.builder()
                .id(bookRequest.id())
                .title(bookRequest.title())
                .isbn(bookRequest.isbn())
                .authorName(bookRequest.authorName())
                .synopsis(bookRequest.synopsis())
                .archived(false)
                .shareable(bookRequest.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .authorName(book.getAuthorName())
                .synopsis(book.getSynopsis())
                .ownerName(book.getOwner().getFullName())
                .cover(readFileFromLocation(book.getBookCover()))
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
