package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Loan;
import com.polstat.perpustakaan.service.LoanService;
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
@RequestMapping("/api/loans")
@Tag(name = "Loan (REST)", description = "Manajemen data peminjaman dan pengembalian buku")
public class LoanController {

    private final LoanService loanService;

    // FIX: Gunakan Constructor Injection
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Operation(summary = "Mendapatkan semua riwayat peminjaman buku.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan daftar peminjaman.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Loan.class)))),
            @ApiResponse(responseCode = "401", description = "Akses tidak sah (Membutuhkan JWT).")
    })
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @Operation(summary = "Mendapatkan riwayat peminjaman berdasarkan ID anggota.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan riwayat peminjaman anggota.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Loan.class)))),
            @ApiResponse(responseCode = "401", description = "Akses tidak sah (Membutuhkan JWT).")
    })
    @GetMapping("/member/{memberId}")
    public List<Loan> getLoansByMember(@Parameter(description = "ID Anggota") @PathVariable Long memberId) {
        return loanService.getLoansByMember(memberId);
    }

    @Operation(summary = "Melakukan peminjaman buku.",
            description = "Membuat entri peminjaman baru.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Peminjaman berhasil dibuat.",
                    content = @Content(schema = @Schema(implementation = Loan.class))),
            @ApiResponse(responseCode = "400", description = "Gagal melakukan peminjaman. Data tidak valid."),
            @ApiResponse(responseCode = "401", description = "Akses tidak sah (Membutuhkan JWT).")
    })
    @PostMapping("/borrow")
    public ResponseEntity<Loan> borrowBook(
            @Parameter(description = "ID Anggota yang meminjam") @RequestParam Long memberId,
            @Parameter(description = "ID Buku yang dipinjam") @RequestParam Long bookId) {
        try {
            Loan loan = loanService.borrowBook(memberId, bookId);
            return ResponseEntity.ok(loan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Mengembalikan buku yang dipinjam.",
            description = "Memperbarui status peminjaman menjadi dikembalikan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buku berhasil dikembalikan.",
                    content = @Content(schema = @Schema(implementation = Loan.class))),
            @ApiResponse(responseCode = "400", description = "Gagal mengembalikan buku. Loan tidak ditemukan atau data tidak valid."),
            @ApiResponse(responseCode = "401", description = "Akses tidak sah (Membutuhkan JWT).")
    })
    @PutMapping("/return/{loanId}")
    public ResponseEntity<Loan> returnBook(@Parameter(description = "ID Peminjaman (Loan)") @PathVariable Long loanId) {
        try {
            Loan loan = loanService.returnBook(loanId);
            return ResponseEntity.ok(loan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            // Menangkap RuntimeException dari service (misalnya "Loan not found")
            return ResponseEntity.badRequest().body(null);
        }
    }
}