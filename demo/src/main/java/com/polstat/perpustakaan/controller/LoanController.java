package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Loan;
import com.polstat.perpustakaan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// IMPORTS BARU
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter; // Digunakan untuk mendokumentasikan @PathVariable/@RequestParam

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loan (REST)", description = "Manajemen data peminjaman buku")
public class LoanController {
    // ... (omitted constructor)

    @Operation(summary = "Mendapatkan semua riwayat peminjaman buku.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan daftar peminjaman.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Loan.class))))
    })
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @Operation(summary = "Mendapatkan riwayat peminjaman berdasarkan ID anggota.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan riwayat peminjaman anggota.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Loan.class))))
    })
    @GetMapping("/member/{memberId}")
    public List<Loan> getLoansByMember(@Parameter(description = "ID Anggota") @PathVariable Long memberId) {
        return loanService.getLoansByMember(memberId);
    }

    @Operation(summary = "Melakukan peminjaman buku.",
            description = "Membuat entri peminjaman baru berdasarkan ID Anggota dan ID Buku.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Peminjaman berhasil dibuat.",
                    content = @Content(schema = @Schema(implementation = Loan.class))),
            @ApiResponse(responseCode = "400", description = "Gagal melakukan peminjaman. Cek memberId dan bookId.")
    })
    @PostMapping("/borrow")
    public ResponseEntity<Loan> borrowBook(
            @Parameter(description = "ID Anggota yang meminjam") @RequestParam Long memberId,
            @Parameter(description = "ID Buku yang dipinjam") @RequestParam Long bookId) {
        // ... (method body)
    }

    @Operation(summary = "Mengembalikan buku yang dipinjam.",
            description = "Memperbarui status peminjaman menjadi dikembalikan (returned=true).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buku berhasil dikembalikan.",
                    content = @Content(schema = @Schema(implementation = Loan.class))),
            @ApiResponse(responseCode = "400", description = "Gagal mengembalikan buku. Loan tidak ditemukan.")
    })
    @PutMapping("/return/{loanId}")
    public ResponseEntity<Loan> returnBook(@Parameter(description = "ID Peminjaman (Loan)") @PathVariable Long loanId) {
        // ... (method body)
    }
}