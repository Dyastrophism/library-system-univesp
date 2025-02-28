package com.univesp.library_system.service;

import com.univesp.library_system.controller.request.BookRequest;
import com.univesp.library_system.domain.Book;
import com.univesp.library_system.domain.User;
import com.univesp.library_system.service.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;

    public Integer saveBook(BookRequest bookRequest, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(bookRequest);
        return null;
    }
}
