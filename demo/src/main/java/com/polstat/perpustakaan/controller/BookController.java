package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Book;
import com.polstat.perpustakaan.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// IMPORTS SPRINGDOC
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book (REST)", description = "Manajemen data buku perpustakaan")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ========== GET ALL BOOKS ==========
    @Operation(summary = "Mendapatkan semua data buku perpustakaan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Berhasil mendapatkan daftar buku.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
            @ApiResponse(responseCode = "401",
                    description = "Akses tidak sah (Membutuhkan JWT).")
    })
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // ========== GET BOOK BY ID ==========
    @Operation(summary = "Mendapatkan data buku berdasarkan ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Berhasil mendapatkan data buku.",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404",
                    description = "Buku tidak ditemukan."),
            @ApiResponse(responseCode = "401",
                    description = "Akses tidak sah (Membutuhkan JWT).")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "ID Buku") @PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== ADD NEW BOOK ==========
    @Operation(summary = "Menambahkan buku baru ke perpustakaan.",
            description = "Menambahkan data buku baru dengan informasi title, author, publisher, year, dan available.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Berhasil menambahkan buku.",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400",
                    description = "Input data tidak valid."),
            @ApiResponse(responseCode = "401",
                    description = "Akses tidak sah (Membutuhkan JWT).")
    })
    @PostMapping
    public Book addBook(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data buku yang akan ditambahkan (title, author, publisher, year, available)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Book.class)))
            @RequestBody Book book) {
        return bookRepository.save(book);
    }

    // ========== UPDATE BOOK ==========
    @Operation(summary = "Memperbarui data buku yang sudah ada.",
            description = "Mengubah informasi buku seperti title, author, publisher, year, atau status available.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Berhasil memperbarui data buku.",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404",
                    description = "Buku tidak ditemukan."),
            @ApiResponse(responseCode = "401",
                    description = "Akses tidak sah (Membutuhkan JWT).")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID Buku yang akan diupdate") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data buku yang diperbarui",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Book.class)))
            @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setPublisher(bookDetails.getPublisher());
                    book.setYear(bookDetails.getYear());
                    book.setAvailable(bookDetails.getAvailable());
                    return ResponseEntity.ok(bookRepository.save(book));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== DELETE BOOK ==========
    @Operation(summary = "Menghapus buku dari perpustakaan.",
            description = "Menghapus data buku berdasarkan ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Berhasil menghapus buku."),
            @ApiResponse(responseCode = "404",
                    description = "Buku tidak ditemukan."),
            @ApiResponse(responseCode = "401",
                    description = "Akses tidak sah (Membutuhkan JWT).")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(
            @Parameter(description = "ID Buku yang akan dihapus") @PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}