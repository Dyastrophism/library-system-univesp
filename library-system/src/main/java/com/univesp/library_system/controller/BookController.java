package com.univesp.library_system.controller;

import com.univesp.library_system.controller.request.BookRequest;
import com.univesp.library_system.controller.response.BookResponse;
import com.univesp.library_system.controller.response.BorrowedBookResponse;
import com.univesp.library_system.controller.response.PageResponse;
import com.univesp.library_system.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(
            @Valid
            @RequestBody
            BookRequest bookRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.saveBook(bookRequest, connectedUser));
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponse> getBook(
            @PathVariable("book-id")
            Integer bookId
    ) {
        return ResponseEntity.ok(bookService.findById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false)
            int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)
            int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooks(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false)
            int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)
            int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false)
            int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)
            int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false)
            int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)
            int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("book-id")
            Integer bookId,
            @RequestParam(name = "shareable")
            boolean shareable,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.updateShareableStatus(bookId, shareable, connectedUser));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("book-id")
            Integer bookId,
            @RequestParam(name = "archived")
            boolean archived,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.updateArchivedStatus(bookId, archived, connectedUser));
    }
}
