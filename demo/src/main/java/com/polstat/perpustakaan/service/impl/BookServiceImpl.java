package com.polstat.perpustakaan.service.impl;

import com.polstat.perpustakaan.dto.BookDto;
import com.polstat.perpustakaan.entity.Book;
import com.polstat.perpustakaan.mapper.BookMapper;
import com.polstat.perpustakaan.repository.BookRepository;
import com.polstat.perpustakaan.service.BookService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.mapToBook(bookDto);
        Book saved = bookRepository.save(book);
        return bookMapper.mapToBookDto(saved);
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book existing = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        existing.setTitle(bookDto.getTitle());
        existing.setAuthor(bookDto.getAuthor());
        existing.setPublisher(bookDto.getPublisher());
        existing.setYear(bookDto.getYear());
        existing.setAvailable(bookDto.getAvailable());
        Book updated = bookRepository.save(existing);
        return bookMapper.mapToBookDto(updated);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::mapToBookDto)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::mapToBookDto)
                .collect(Collectors.toList());
    }
}
