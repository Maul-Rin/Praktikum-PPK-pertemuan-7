package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Loan;
import com.polstat.perpustakaan.service.BorrowingService;
import org.springframework.web.bind.annotation.*;

// IMPORTS SPRINGDOC (PASTIKAN SEMUA INI ADA)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter; // <-- YANG PALING KRUSIAL

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
@Tag(name = "Borrowing (REST - Praktikum 6 Version)", description = "Manajemen data peminjaman buku (versi alternatif)")
public class BorrowingController {

    private final BorrowingService borrowingService; // <-- Pastikan ini ada

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    // ... (Lanjutkan dengan kode yang sudah saya berikan di jawaban sebelumnya,
    // pastikan semua anotasi Springdoc sudah diterapkan dengan benar)

    @Operation(summary = "Mendapatkan semua riwayat peminjaman buku (Borrowing).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan daftar peminjaman.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Loan.class))))
    })
    @GetMapping
    public List<Loan> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }
    // ... (metode lainnya)
}