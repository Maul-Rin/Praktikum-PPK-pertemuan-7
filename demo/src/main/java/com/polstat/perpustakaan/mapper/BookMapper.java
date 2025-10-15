package com.polstat.perpustakaan.mapper;

import com.polstat.perpustakaan.dto.BookDto;
import com.polstat.perpustakaan.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    // Entity → DTO
    public BookDto mapToBookDto(Book book) {
        if (book == null) return null;
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .year(book.getYear())
                .available(book.getAvailable()) // Boolean getter
                .build();
    }

    // DTO → Entity
    public Book mapToBook(BookDto bookDto) {
        if (bookDto == null) return null;
        return Book.builder()
                .id(bookDto.getId())
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .publisher(bookDto.getPublisher())
                .year(bookDto.getYear())
                .available(Boolean.TRUE.equals(bookDto.getAvailable())) // handle null
                .build();
    }
}
