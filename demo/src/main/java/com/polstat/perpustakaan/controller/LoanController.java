package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Loan;
import com.polstat.perpustakaan.service.LoanService;
import org.springframework.http.ResponseEntity; // <-- PASTIKAN INI ADA
import org.springframework.web.bind.annotation.*; // <-- PASTIKAN INI ADA

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
@RequestMapping("/api/loans")
@Tag(name = "Loan (REST)", description = "Manajemen data peminjaman buku")
public class LoanController {

    private final LoanService loanService; // <-- Pastikan ini ada

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // ... (metode lainnya dengan anotasi Springdoc yang sudah benar)
}