package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Loan;
import com.polstat.perpustakaan.service.BorrowingService;
import org.springframework.web.bind.annotation.*;
// IMPORTS BARU
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
@Tag(name = "Borrowing (REST - Praktikum 6 Version)", description = "Manajemen data peminjaman buku (versi alternatif)")
public class BorrowingController {
    // ... (omitted constructor)

    @Operation(summary = "Mendapatkan semua riwayat peminjaman buku (Borrowing).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan daftar peminjaman.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Loan.class))))
    })
    @GetMapping
    public List<Loan> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }

    @Operation(summary = "Mendapatkan riwayat peminjaman berdasarkan ID anggota (Borrowing).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan riwayat peminjaman anggota.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Loan.class))))
    })
    @GetMapping("/member/{memberId}")
    public List<Loan> getBorrowingsByMember(@Parameter(description = "ID Anggota") @PathVariable Long memberId) {
        return borrowingService.getBorrowingsByMember(memberId);
    }

    @Operation(summary = "Melakukan peminjaman buku (Borrowing).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Peminjaman berhasil dibuat.",
                    content = @Content(schema = @Schema(implementation = Loan.class))),
            @ApiResponse(responseCode = "400", description = "Gagal melakukan peminjaman. Cek memberId dan bookId.")
    })
    @PostMapping("/borrow/{memberId}/{bookId}")
    public Loan borrowBook(
            @Parameter(description = "ID Anggota") @PathVariable Long memberId,
            @Parameter(description = "ID Buku") @PathVariable Long bookId) {
        return borrowingService.borrowBook(memberId, bookId);
    }

    @Operation(summary = "Mengembalikan buku yang dipinjam (Borrowing).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buku berhasil dikembalikan.",
                    content = @Content(schema = @Schema(implementation = Loan.class))),
            @ApiResponse(responseCode = "400", description = "Gagal mengembalikan buku. Loan tidak ditemukan.")
    })
    @PostMapping("/return/{loanId}")
    public Loan returnBook(@Parameter(description = "ID Peminjaman (Loan)") @PathVariable Long loanId) {
        return borrowingService.returnBook(loanId);
    }
}