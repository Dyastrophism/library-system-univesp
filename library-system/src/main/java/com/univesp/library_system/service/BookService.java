package com.univesp.library_system.service;

import com.univesp.library_system.controller.request.BookRequest;
import com.univesp.library_system.controller.response.BookResponse;
import com.univesp.library_system.controller.response.BorrowedBookResponse;
import com.univesp.library_system.controller.response.PageResponse;
import com.univesp.library_system.domain.Book;
import com.univesp.library_system.domain.BookTransactionHistory;
import com.univesp.library_system.domain.User;
import com.univesp.library_system.handler.OperationNotPermittedException;
import com.univesp.library_system.repository.BookRepository;
import com.univesp.library_system.repository.BookTransactionHistoryRepository;
import com.univesp.library_system.service.mapper.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.univesp.library_system.domain.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

    /**
     * Save book
     * @param bookRequest book request
     * @param connectedUser connected user
     * @return book id
     */
    public Integer saveBook(BookRequest bookRequest, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    /**
     * Find book by id
     * @param bookId book id
     * @return book response
     */
    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    /**
     * Find all books by owner
     * @param page page
     * @param size size
     * @param connectedUser connected user
     * @return page response of books
     */
    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    /**
     * Find all books by owner
     * @param page page
     * @param size size
     * @param connectedUser connected user
     * @return page response of books
     */
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    /**
     * Find all borrowed books
     * @param page page
     * @param size size
     * @param connectedUser connected user
     * @return page response of borrowed books
     */
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks =
                bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponse =
                allBorrowedBooks.stream()
                        .map(bookMapper::toBorrowedBookResponse)
                        .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    /**
     * Find all returned books
     * @param page page
     * @param size size
     * @param connectedUser connected user
     * @return page response of borrowed books
     */
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks =
                bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponse =
                allBorrowedBooks.stream()
                        .map(bookMapper::toBorrowedBookResponse)
                        .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    /**
     * Update shareable status
     * @param bookId book id
     * @param shareable shareable status
     * @param connectedUser connected user
     * @return book id
     */
    public Integer updateShareableStatus(Integer bookId, boolean shareable, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID::" + bookId));
        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You are not the owner of this book");
        }
        book.setShareable(shareable);
        return bookRepository.save(book).getId();
    }

    /**
     * Update archived status
     * @param bookId book id
     * @param archived archived status
     * @param connectedUser connected user
     * @return book id
     */
    public Integer updateArchivedStatus(Integer bookId, boolean archived, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID::" + bookId));
        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You are not the owner of this book");
        }
        book.setArchived(archived);
        return bookRepository.save(book).getId();
    }

    /**
     * Borrow book
     * @param bookId book id
     * @param connectedUser connected user
     * @return book transaction history id
     */
    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID::" + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("This book is not available for borrowing");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You are the owner of this book");
        }
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The book is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory =
                BookTransactionHistory.builder()
                        .user(user)
                        .book(book)
                        .returned(false)
                        .returnApproved(false)
                        .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    /**
     * Return borrowed book
     * @param bookId book id
     * @param connectedUser connected user
     * @return book transaction history id
     */
    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID::" + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("This book is not available for borrowing");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }
        BookTransactionHistory bookTransactionHistory =
                bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book with ID::" + bookId));
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    /**
     * Approve return borrowed book
     * @param bookId book id
     * @param connectedUser connected user
     * @return book transaction history id
     */
    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found::" + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("This book is not available for borrowing");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }
        BookTransactionHistory bookTransactionHistory =
                bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet with ID::" + bookId));
        bookTransactionHistory.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    /**
     * Upload book cover picture
     * @param bookId book id
     * @param file file
     * @param connectedUser connected user
     */
    public void uploadBookCoverPicture(Integer bookId, MultipartFile file, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID::" + bookId));
        User user = ((User) connectedUser.getPrincipal());
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }
}
