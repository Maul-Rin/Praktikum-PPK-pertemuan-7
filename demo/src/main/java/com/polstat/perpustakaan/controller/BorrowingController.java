package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Borrowing;
import com.polstat.perpustakaan.repository.BorrowingRepository; // <-- IMPORT BARU
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
@RequestMapping("/api/borrowings")
@Tag(name = "Borrowing", description = "Manajemen data peminjaman buku (Controller langsung ke Repository)")
public class BorrowingController {

    // Ganti BorrowingService dengan BorrowingRepository
    private final BorrowingRepository borrowingRepository;

    // Jika Anda ingin implementasi pinjam/kembali, Anda mungkin perlu Repository lain:
    // private final UserRepository userRepository;
    // private final BookRepository bookRepository;

    public BorrowingController(BorrowingRepository borrowingRepository) {
        this.borrowingRepository = borrowingRepository;
    }

    // --- GET ALL BORROWINGS ---

    @Operation(summary = "Mendapatkan semua riwayat peminjaman buku (Borrowing).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan daftar peminjaman.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Borrowing.class))))
    })
    @GetMapping
    public List<Borrowing> getAllBorrowings() {
        // Logika dari Service Layer dipindahkan ke sini:
        return borrowingRepository.findAll();
    }

    // --- GET BORROWING BY ID ---

    @Operation(summary = "Mendapatkan detail peminjaman berdasarkan ID-nya.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan detail peminjaman.",
                    content = @Content(schema = @Schema(implementation = Borrowing.class))),
            @ApiResponse(responseCode = "404", description = "Peminjaman tidak ditemukan.",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Borrowing> getBorrowingById(
            @Parameter(description = "ID Peminjaman yang akan dicari", example = "1")
            @PathVariable Long id) {

        // Logika dari Service Layer dipindahkan ke sini:
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Peminjaman dengan ID " + id + " tidak ditemukan.")
                );
        return ResponseEntity.ok(borrowing);
    }

    // --- BORROW BOOK (CREATE BORROWING) ---

    @Operation(summary = "Membuat peminjaman buku baru (Borrowing).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Peminjaman berhasil dibuat.",
                    content = @Content(schema = @Schema(implementation = Borrowing.class))),
            @ApiResponse(responseCode = "400", description = "Permintaan tidak valid (misalnya, stok buku habis).",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Borrowing> borrowBook(
            @Parameter(description = "ID Pengguna yang meminjam", example = "1", required = true)
            @RequestParam Long userId,
            @Parameter(description = "ID Buku yang dipinjam", example = "10", required = true)
            @RequestParam Long bookId) {

        // CATATAN: Logika bisnis di sini akan menjadi sangat kompleks
        // karena harus mencakup pengecekan User, Book, stok, dll.
        // Untuk contoh ini, kita asumsikan Borrowing dibuat dari Request DTO/paramater.

        // Logika SANGAT SEDERHANA (Hanya menyimpan data dummy, TIDAK disarankan untuk produksi):
        try {
            Borrowing newBorrowing = new Borrowing();
            // Asumsi Borrowing memiliki setter (setUserId, setBookId, setBorrowDate, dll)
            // newBorrowing.setUserId(userId);
            // newBorrowing.setBookId(bookId);
            // newBorrowing.setBorrowDate(java.time.LocalDate.now());
            // newBorrowing.setStatus("BORROWED");

            // Logika pinjam yang sesungguhnya (membutuhkan entitas User dan Book):
            // 1. Cek User dan Book dari repository
            // 2. Cek Stok Book
            // 3. Update Stok Book
            // 4. Set relasi di newBorrowing

            Borrowing savedBorrowing = borrowingRepository.save(newBorrowing);
            return new ResponseEntity<>(savedBorrowing, HttpStatus.CREATED);
        } catch (Exception e) {
            // Tangani error jika gagal menyimpan atau logika bisnis gagal
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gagal meminjam buku: " + e.getMessage());
        }
    }

    // --- RETURN BOOK (UPDATE BORROWING) ---

    @Operation(summary = "Mengembalikan buku yang dipinjam (Menyelesaikan Borrowing).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pengembalian berhasil.",
                    content = @Content(schema = @Schema(implementation = Borrowing.class))),
            @ApiResponse(responseCode = "400", description = "Peminjaman sudah dikembalikan atau ID tidak valid.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Peminjaman tidak ditemukan.",
                    content = @Content)
    })
    @PutMapping("/{borrowingId}/return")
    public ResponseEntity<Borrowing> returnBook(
            @Parameter(description = "ID Peminjaman yang akan dikembalikan", example = "1")
            @PathVariable Long borrowingId) {

        // Logika pengembalian (dipindahkan dari Service ke sini):
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Peminjaman dengan ID " + borrowingId + " tidak ditemukan.")
                );

        // Logika bisnis:
        // if ("RETURNED".equals(borrowing.getStatus())) {
        //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Buku sudah dikembalikan.");
        // }

        // borrowing.setStatus("RETURNED");
        // borrowing.setReturnDate(java.time.LocalDate.now());

        // ... Logika bisnis: tambahkan stok buku kembali ke Book Repository ...

        try {
            Borrowing returnedBorrowing = borrowingRepository.save(borrowing);
            return ResponseEntity.ok(returnedBorrowing);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gagal mengembalikan buku: " + e.getMessage());
        }
    }
}